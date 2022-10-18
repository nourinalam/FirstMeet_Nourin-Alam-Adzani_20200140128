/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cemilanpws.koneksidatabase;

import java.io.Serializable;
import javax.persistence.Basic;
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
@Table(name = "manager")
@NamedQueries({
    @NamedQuery(name = "Manager.findAll", query = "SELECT m FROM Manager m"),
    @NamedQuery(name = "Manager.findByIdManager", query = "SELECT m FROM Manager m WHERE m.idManager = :idManager"),
    @NamedQuery(name = "Manager.findByNamaManager", query = "SELECT m FROM Manager m WHERE m.namaManager = :namaManager"),
    @NamedQuery(name = "Manager.findByJenisKelamin", query = "SELECT m FROM Manager m WHERE m.jenisKelamin = :jenisKelamin"),
    @NamedQuery(name = "Manager.findByAlamat", query = "SELECT m FROM Manager m WHERE m.alamat = :alamat"),
    @NamedQuery(name = "Manager.findByNoTelp", query = "SELECT m FROM Manager m WHERE m.noTelp = :noTelp")})
public class Manager implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_manager")
    private String idManager;
    @Basic(optional = false)
    @Column(name = "nama_manager")
    private String namaManager;
    @Basic(optional = false)
    @Column(name = "jenis_kelamin")
    private Character jenisKelamin;
    @Basic(optional = false)
    @Column(name = "alamat")
    private String alamat;
    @Basic(optional = false)
    @Column(name = "no_telp")
    private String noTelp;
    @JoinColumn(name = "id_toko", referencedColumnName = "id_toko")
    @OneToOne(optional = false)
    private Toko idToko;

    public Manager() {
    }

    public Manager(String idManager) {
        this.idManager = idManager;
    }

    public Manager(String idManager, String namaManager, Character jenisKelamin, String alamat, String noTelp) {
        this.idManager = idManager;
        this.namaManager = namaManager;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.noTelp = noTelp;
    }

    public String getIdManager() {
        return idManager;
    }

    public void setIdManager(String idManager) {
        this.idManager = idManager;
    }

    public String getNamaManager() {
        return namaManager;
    }

    public void setNamaManager(String namaManager) {
        this.namaManager = namaManager;
    }

    public Character getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(Character jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
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

    public Toko getIdToko() {
        return idToko;
    }

    public void setIdToko(Toko idToko) {
        this.idToko = idToko;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idManager != null ? idManager.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Manager)) {
            return false;
        }
        Manager other = (Manager) object;
        if ((this.idManager == null && other.idManager != null) || (this.idManager != null && !this.idManager.equals(other.idManager))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cemilanpws.koneksidatabase.Manager[ idManager=" + idManager + " ]";
    }
    
}
