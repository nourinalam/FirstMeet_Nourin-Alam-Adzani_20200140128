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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@Table(name = "customer")
@NamedQueries({
    @NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c"),
    @NamedQuery(name = "Customer.findByIdCustomer", query = "SELECT c FROM Customer c WHERE c.idCustomer = :idCustomer"),
    @NamedQuery(name = "Customer.findByNamaCustomer", query = "SELECT c FROM Customer c WHERE c.namaCustomer = :namaCustomer"),
    @NamedQuery(name = "Customer.findByJenisKelamin", query = "SELECT c FROM Customer c WHERE c.jenisKelamin = :jenisKelamin"),
    @NamedQuery(name = "Customer.findByAlamat", query = "SELECT c FROM Customer c WHERE c.alamat = :alamat"),
    @NamedQuery(name = "Customer.findByNoTelp", query = "SELECT c FROM Customer c WHERE c.noTelp = :noTelp")})
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_customer")
    private String idCustomer;
    @Basic(optional = false)
    @Column(name = "nama_customer")
    private String namaCustomer;
    @Basic(optional = false)
    @Column(name = "jenis_kelamin")
    private Character jenisKelamin;
    @Basic(optional = false)
    @Column(name = "alamat")
    private String alamat;
    @Basic(optional = false)
    @Column(name = "no_telp")
    private String noTelp;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idCustomer")
    private Daftarmenu daftarmenu;

    public Customer() {
    }

    public Customer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Customer(String idCustomer, String namaCustomer, Character jenisKelamin, String alamat, String noTelp) {
        this.idCustomer = idCustomer;
        this.namaCustomer = namaCustomer;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.noTelp = noTelp;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNamaCustomer() {
        return namaCustomer;
    }

    public void setNamaCustomer(String namaCustomer) {
        this.namaCustomer = namaCustomer;
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

    public Daftarmenu getDaftarmenu() {
        return daftarmenu;
    }

    public void setDaftarmenu(Daftarmenu daftarmenu) {
        this.daftarmenu = daftarmenu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCustomer != null ? idCustomer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.idCustomer == null && other.idCustomer != null) || (this.idCustomer != null && !this.idCustomer.equals(other.idCustomer))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cemilanpws.koneksidatabase.Customer[ idCustomer=" + idCustomer + " ]";
    }
    
}
