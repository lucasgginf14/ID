package gei.id.tutelado.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@TableGenerator(name = "xeradorIdsPersoas", table = "taboa_ids",
        pkColumnName = "nome_id", valueColumnName = "ultimo_valor_id",
        pkColumnValue = "idPersoa", initialValue = 0, allocationSize = 1)


@NamedQueries({
        @NamedQuery(name = "Persoa.recuperaPorDni",
                query = "SELECT p FROM Persoa p WHERE p.dni = :dni")
})

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_persoa")
public abstract class Persoa {

    @Id
    @GeneratedValue(generator = "xeradorIdsPersoas")
    private Long id;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(nullable = false)
    private String nome;

    private LocalDate dataNacemento;


    @ManyToMany(mappedBy = "persoas", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Viaxe> viaxes = new HashSet<>();


    public Persoa() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNacemento() {
        return dataNacemento;
    }

    public void setDataNacemento(LocalDate dataNacemento) {
        this.dataNacemento = dataNacemento;
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
        if (o == null || getClass().getSuperclass() != o.getClass().getSuperclass())
            return false;
        Persoa persoa = (Persoa) o;
        if (dni != null) {
            return dni.equals(persoa.dni);
        } else {
            return persoa.dni == null;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dni == null) ? 0 : dni.hashCode());
        return result;
    }
}