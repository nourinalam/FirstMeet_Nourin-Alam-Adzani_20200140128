/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cemilanpws.koneksidatabase;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@Table(name = "toko")
@NamedQueries({
    @NamedQuery(name = "Toko.findAll", query = "SELECT t FROM Toko t"),
    @NamedQuery(name = "Toko.findByIdToko", query = "SELECT t FROM Toko t WHERE t.idToko = :idToko"),
    @NamedQuery(name = "Toko.findByNamaToko", query = "SELECT t FROM Toko t WHERE t.namaToko = :namaToko"),
    @NamedQuery(name = "Toko.findByAlamat", query = "SELECT t FROM Toko t WHERE t.alamat = :alamat"),
    @NamedQuery(name = "Toko.findByNoTelp", query = "SELECT t FROM Toko t WHERE t.noTelp = :noTelp")})
public class Toko implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_toko")
    private String idToko;
    @Basic(optional = false)
    @Column(name = "nama_toko")
    private String namaToko;
    @Basic(optional = false)
    @Column(name = "alamat")
    private String alamat;
    @Basic(optional = false)
    @Column(name = "no_telp")
    private String noTelp;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idToko")
    private Manager manager;
    @JoinColumn(name = "id_menu", referencedColumnName = "id_menu")
    @OneToOne(optional = false)
    private Daftarmenu idMenu;

    public Toko() {
    }

    public Toko(String idToko) {
        this.idToko = idToko;
    }

    public Toko(String idToko, String namaToko, String alamat, String noTelp) {
        this.idToko = idToko;
        this.namaToko = namaToko;
        this.alamat = alamat;
        this.noTelp = noTelp;
    }

    public String getIdToko() {
        return idToko;
    }

    public void setIdToko(String idToko) {
        this.idToko = idToko;
    }

    public String getNamaToko() {
        return namaToko;
    }

    public void setNamaToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Daftarmenu getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Daftarmenu idMenu) {
        this.idMenu = idMenu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idToko != null ? idToko.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Toko)) {
            return false;
        }
        Toko other = (Toko) object;
        if ((this.idToko == null && other.idToko != null) || (this.idToko != null && !this.idToko.equals(other.idToko))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cemilanpws.koneksidatabase.Toko[ idToko=" + idToko + " ]";
    }
    
}
