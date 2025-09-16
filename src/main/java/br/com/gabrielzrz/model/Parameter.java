package br.com.gabrielzrz.model;

import br.com.gabrielzrz.enums.DataType;
import br.com.gabrielzrz.enums.ParameterType;
import br.com.gabrielzrz.model.base.BaseEntityVersioned;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Zorzi
 */
@Entity
@Audited
@Table(name = "parameter", uniqueConstraints = @UniqueConstraint(name = "uk_parameter_name_branch", columnNames = {"name", "branch_id"}))
public class Parameter extends BaseEntityVersioned implements Serializable {

    @Column(name = "parameter_type", length = 100, nullable = false)
    @Enumerated(EnumType.STRING)
    private ParameterType parameterType;

    @Column(name = "value")
    private String value;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "data_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    public Parameter() {
    }

    public Parameter(ParameterType parameterType, String value, DataType dataType, Branch branch) {
        this.parameterType = parameterType;
        this.value = value;
        this.dataType = dataType;
        this.branch = branch;
    }

    // Getters && Setters
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ParameterType getParameterType() {
        return parameterType;
    }

    public void setParameterType(ParameterType parameterType) {
        this.parameterType = parameterType;
    }

    // Equals && HashCode
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Parameter parameter = (Parameter) o;
        return Objects.equals(id, parameter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
