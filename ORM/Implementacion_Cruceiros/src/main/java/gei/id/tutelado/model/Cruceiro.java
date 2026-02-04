package gei.id.tutelado.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@TableGenerator(name = "xeradorIdsCruceiros", table = "taboa_ids",
        pkColumnName = "nome_id", valueColumnName = "ultimo_valor_id",
        pkColumnValue = "idCruceiro", initialValue = 0, allocationSize = 1)


@NamedQueries({
        @NamedQuery(name = "Cruceiro.recuperaPorCodigo",
                query = "SELECT c FROM Cruceiro c WHERE c.codigo = :codigo")
})

@Entity
public class Cruceiro {

    @Id
    @GeneratedValue(generator = "xeradorIdsCruceiros")
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String nome;

    private int capacidade;

    @OneToMany(mappedBy = "cruceiro", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Viaxe> viaxes = new HashSet<>();

    public Cruceiro() {
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public Set<Viaxe> getViaxes() {
        return viaxes;
    }

    public void setViaxes(Set<Viaxe> viaxes) {
        this.viaxes = viaxes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cruceiro cruceiro = (Cruceiro) o;
        if (codigo == null) {
            return cruceiro.codigo == null;
        } else return codigo.equals(cruceiro.codigo);
    }

    @Override
    public int hashCode() {
        final int prime = 71;
        int result = 1;
        result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
        return result;
    }
}