package javafxmvc.model.domain;

public class Funcao {
    private int idFuncao;
    private String nomeFuncao;


    public int getIdFuncao() {
        return idFuncao;
    }

    public void setIdFuncao(int idFuncao) {
        this.idFuncao = idFuncao;
    }

    public String getNomeFuncao() {
        return nomeFuncao;
    }

    public void setNomeFuncao(String nomeFuncao) {
        this.nomeFuncao = nomeFuncao;
    }

    public String toString(){
        return this.nomeFuncao;
    }
}
