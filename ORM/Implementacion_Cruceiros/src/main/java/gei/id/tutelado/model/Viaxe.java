package gei.id.tutelado.model;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderColumn;
import javax.persistence.TableGenerator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@TableGenerator(name = "xeradorIdsViaxes", table = "taboa_ids",
        pkColumnName = "nome_id", valueColumnName = "ultimo_valor_id",
        pkColumnValue = "idViaxe", initialValue = 0, allocationSize = 1)


@NamedQueries({
        @NamedQuery(name = "Viaxe.recuperaPorCodigo",
                query = "SELECT v FROM Viaxe v WHERE v.codigo = :codigo")
})

@Entity
public class Viaxe {

    @Id
    @GeneratedValue(generator = "xeradorIdsViaxes")
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    private LocalDate dataInicio;

    private LocalDate dataFin;

    @ElementCollection(fetch = FetchType.EAGER) // + sinxelo para os tests
    @CollectionTable(name = "itinerario_portos", joinColumns = @JoinColumn(name = "id_viaxe"))
    @Column(name = "porto", nullable = false)
    @OrderColumn
    private List<String> itinerarioPortos = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_cruceiro")
    private Cruceiro cruceiro;


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "persoa_viaxe",
            joinColumns = @JoinColumn(name = "id_viaxe"),
            inverseJoinColumns = @JoinColumn(name = "id_persoa"))
    private Set<Persoa> persoas = new HashSet<>();

    public Viaxe() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFin() {
        return dataFin;
    }

    public void setDataFin(LocalDate dataFin) {
        this.dataFin = dataFin;
    }

    public List<String> getItinerarioPortos() {
        return itinerarioPortos;
    }

    public void setItinerarioPortos(List<String> itinerarioPortos) {
        this.itinerarioPortos = itinerarioPortos;
    }

    public Cruceiro getCruceiro() {
        return cruceiro;
    }

    public void setCruceiro(Cruceiro cruceiro) {
        this.cruceiro = cruceiro;
    }

    public Set<Persoa> getPersoas() {
        return persoas;
    }

    public void setPersoas(Set<Persoa> persoas) {
        this.persoas = persoas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Viaxe viaxe = (Viaxe) o;
        if (codigo == null) {
            return viaxe.codigo == null;
        } else return codigo.equals(viaxe.codigo);
    }

    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 1;
        result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
        return result;
    }
}