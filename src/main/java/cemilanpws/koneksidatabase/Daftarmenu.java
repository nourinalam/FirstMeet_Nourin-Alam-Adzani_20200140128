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
@Table(name = "daftarmenu")
@NamedQueries({
    @NamedQuery(name = "Daftarmenu.findAll", query = "SELECT d FROM Daftarmenu d"),
    @NamedQuery(name = "Daftarmenu.findByIdMenu", query = "SELECT d FROM Daftarmenu d WHERE d.idMenu = :idMenu"),
    @NamedQuery(name = "Daftarmenu.findByNamaMakanan", query = "SELECT d FROM Daftarmenu d WHERE d.namaMakanan = :namaMakanan"),
    @NamedQuery(name = "Daftarmenu.findByJenisMenu", query = "SELECT d FROM Daftarmenu d WHERE d.jenisMenu = :jenisMenu"),
    @NamedQuery(name = "Daftarmenu.findByHarga", query = "SELECT d FROM Daftarmenu d WHERE d.harga = :harga")})
public class Daftarmenu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_menu")
    private String idMenu;
    @Basic(optional = false)
    @Column(name = "nama_makanan")
    private String namaMakanan;
    @Basic(optional = false)
    @Column(name = "jenis_menu")
    private String jenisMenu;
    @Basic(optional = false)
    @Column(name = "harga")
    private int harga;
    @JoinColumn(name = "id_customer", referencedColumnName = "id_customer")
    @OneToOne(optional = false)
    private Customer idCustomer;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idMenu")
    private Toko toko;

    public Daftarmenu() {
    }

    public Daftarmenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public Daftarmenu(String idMenu, String namaMakanan, String jenisMenu, int harga) {
        this.idMenu = idMenu;
        this.namaMakanan = namaMakanan;
        this.jenisMenu = jenisMenu;
        this.harga = harga;
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public String getNamaMakanan() {
        return namaMakanan;
    }

    public void setNamaMakanan(String namaMakanan) {
        this.namaMakanan = namaMakanan;
    }

    public String getJenisMenu() {
        return jenisMenu;
    }

    public void setJenisMenu(String jenisMenu) {
        this.jenisMenu = jenisMenu;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public Customer getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Customer idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Toko getToko() {
        return toko;
    }

    public void setToko(Toko toko) {
        this.toko = toko;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMenu != null ? idMenu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Daftarmenu)) {
            return false;
        }
        Daftarmenu other = (Daftarmenu) object;
        if ((this.idMenu == null && other.idMenu != null) || (this.idMenu != null && !this.idMenu.equals(other.idMenu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cemilanpws.koneksidatabase.Daftarmenu[ idMenu=" + idMenu + " ]";
    }
    
}
