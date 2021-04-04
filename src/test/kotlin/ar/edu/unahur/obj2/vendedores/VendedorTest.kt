package ar.edu.unahur.obj2.vendedor

import ar.edu.unahur.obj2.vendedores.*

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class VendedorTest : DescribeSpec({
  val misiones = Provincia(1300000)
  val sanIgnacio = Ciudad(misiones)
  val cordoba = Provincia(2000000)
  val villaDolores = Ciudad(cordoba)

  describe("Vendedor fijo") {
    val obera = Ciudad(misiones)
    val vendedorFijo = VendedorFijo(obera)

    describe("puedeTrabajarEn") {
      it("su ciudad de origen") {
        vendedorFijo.puedeTrabajarEn(obera).shouldBeTrue()
      }
      it("otra ciudad") {
        vendedorFijo.puedeTrabajarEn(sanIgnacio).shouldBeFalse()
      }

    }
    describe("Vendedor fijo es un vendedor"){
      it("es firme"){
        vendedorFijo.agregarCertificacion(Certificacion(true,5000))
        vendedorFijo.agregarCertificacion(Certificacion(true,5000))
        vendedorFijo.agregarCertificacion(Certificacion(false,2000))
        vendedorFijo.agregarCertificacion(Certificacion(false,2000))

        vendedorFijo.esFirme().shouldBeTrue()
      }

      it("es versatil"){
        vendedorFijo.agregarCertificacion(Certificacion(true,5000))
        vendedorFijo.agregarCertificacion(Certificacion(true,5000))
        vendedorFijo.agregarCertificacion(Certificacion(false,2000))

        vendedorFijo.esVersatil().shouldBeTrue()
      }
    }
  }

  describe("Viajante") {
    val cordoba = Provincia(2000000)
    val villaDolores = Ciudad(cordoba)
    val viajante = Viajante(listOf(misiones))

    describe("puedeTrabajarEn") {
      it("una ciudad que pertenece a una provincia habilitada") {
        viajante.puedeTrabajarEn(sanIgnacio).shouldBeTrue()
      }
      it("una ciudad que no pertenece a una provincia habilitada") {
        viajante.puedeTrabajarEn(villaDolores).shouldBeFalse()
      }
    }
  }

  describe("ComercioCorresponsal"){
    val buenosAires = Provincia(4000000)
    val hurlingham = Ciudad(buenosAires)
    val comercio = ComercioCorresponsal(listOf(hurlingham,sanIgnacio))

    describe("puede trabajar en"){
      it("la ciudad en hurlingham"){
        comercio.puedeTrabajarEn(hurlingham)
      }
    }
    describe("puede"){
      it("no es influyente"){
        comercio.esInfluyente().shouldBeFalse()
      }

      it("que las provincias donde esta son 2 por ende no es influyente "){
        (comercio.provinciasDeLaCiudadesDondeTieneSucursales().size ==2).shouldBeTrue()
      }
    }

  }

  describe("CentroDeDistribucion"){

    val viajante = Viajante(listOf(misiones))
    val viajante2 = Viajante(listOf(cordoba))
    val viajanteNuevo = Viajante(listOf(misiones,cordoba))

    val obera = Ciudad(misiones)
    val vendedorFijo = VendedorFijo(obera)
    vendedorFijo.agregarCertificacion(Certificacion(true,5000))

    val vendedoresDeCentro = mutableListOf<Vendedor>()
    vendedoresDeCentro.add(viajante)
    vendedoresDeCentro.add(viajante2)
    vendedoresDeCentro.add(vendedorFijo)

    val centro = CentroDeDistribucion(villaDolores,vendedoresDeCentro)

    describe("puedeQue"){
        it("los vendedores que tiene el centro son 3"){
          (centro.vendedores.size == 3).shouldBeTrue()
        }
        it("se agrega un vendedor y ahora el centro tiene 4"){
          centro.agregarUnVendedor(viajanteNuevo)
          (centro.vendedores.size == 4).shouldBeTrue()

        }
        it("el vendedor estrella es el vendedorFijo"){

          (centro.vendedorEstrella()==vendedorFijo).shouldBeTrue()
        }

      it("el vendedor estrella no es el viajante"){
        (centro.vendedorEstrella()==viajante).shouldBeFalse()
      }
    }
  }
})
