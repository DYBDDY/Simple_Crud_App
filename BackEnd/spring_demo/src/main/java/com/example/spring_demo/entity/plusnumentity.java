package com.example.spring_demo.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "plusnumentity")  // 指定表格名稱
public class plusnumentity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer number;
    private Integer number2;
    private Integer result;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getnumber() {
        return number;
    }

    public void setnumber(Integer number) {
        this.number = number;
    }

    public Integer getnumber2() {
        return number2;
    }

    public void setnumber2(Integer number2) {
        this.number2 = number2;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

}
