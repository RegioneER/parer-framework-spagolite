package it.eng.integriam.server.ws.reputente;

/**
 *
 * @author Gilioli_P
 */
public class OrganizAbil {

    private Integer idOrganizApplicAbil;
    private boolean flOrganizDefault;
    private ListaServiziAutor listaServiziAutor;
    private ListaTipiDatoAbil listaTipiDatoAbil;

    public Integer getIdOrganizApplicAbil() {
        return idOrganizApplicAbil;
    }

    public void setIdOrganizApplicAbil(Integer idOrganizApplicAbil) {
        this.idOrganizApplicAbil = idOrganizApplicAbil;
    }

    public ListaServiziAutor getListaServiziAutor() {
        if (listaServiziAutor == null) {
            listaServiziAutor = new ListaServiziAutor();
        }
        return listaServiziAutor;
    }

    public void setListaServiziAutor(ListaServiziAutor listaServiziAutor) {
        this.listaServiziAutor = listaServiziAutor;
    }

    public ListaTipiDatoAbil getListaTipiDatoAbil() {
        if (listaTipiDatoAbil == null) {
            listaTipiDatoAbil = new ListaTipiDatoAbil();
        }
        return listaTipiDatoAbil;
    }

    public void setListaTipiDatoAbil(ListaTipiDatoAbil listaTipiDatoAbil) {
        this.listaTipiDatoAbil = listaTipiDatoAbil;
    }

    public boolean isFlOrganizDefault() {
        return flOrganizDefault;
    }

    public void setFlOrganizDefault(boolean flOrganizDefault) {
        this.flOrganizDefault = flOrganizDefault;
    }
}