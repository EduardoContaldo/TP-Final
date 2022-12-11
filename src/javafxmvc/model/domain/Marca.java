package javafxmvc.model.domain;

public class Marca {
    int idMarca;
    String nomeMarca;

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNomeMarca() {
        return nomeMarca;
    }

    public void setNomeMarca(String nomeMarca) {
        this.nomeMarca = nomeMarca;
    }

    public String toString(){
        return this.getNomeMarca();
    }

}
