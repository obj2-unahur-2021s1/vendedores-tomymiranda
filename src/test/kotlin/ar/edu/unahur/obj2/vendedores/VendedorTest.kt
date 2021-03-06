package ar.edu.unahur.obj2.vendedor

import ar.edu.unahur.obj2.vendedores.*
import io.kotest.assertions.throwables.shouldThrowAny

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class VendedorTest : DescribeSpec({
  val misiones = Provincia(1300000)
  val sanIgnacio = Ciudad(misiones)
  val cordoba = Provincia(2000000)
  val villaDolores = Ciudad(cordoba)
  val buenosAires = Provincia(8000000)
  val hurlingham = Ciudad(buenosAires)
  val mendoza = Provincia(2500000)
  val guaymallen = Ciudad(mendoza)

  val certificacionProducto = Certificacion(true,50)
  val certificacionDeNoProducto = Certificacion(false,30)

  describe("Vendedor fijo") {
    val obera = Ciudad(misiones)
    val vendedorFijo = VendedorFijo(obera)
    val vendedorFijo2 = VendedorFijo(hurlingham)

    describe("puedeTrabajarEn") {
      it("su ciudad de origen") {
        vendedorFijo.puedeTrabajarEn(obera).shouldBeTrue()
      }
      it("otra ciudad") {
        vendedorFijo.puedeTrabajarEn(sanIgnacio).shouldBeFalse()
      }

    }
    describe("Vendedor fijo es"){

      it("es firme"){
          vendedorFijo.agregarCertificacion(Certificacion(true,5000))
          vendedorFijo.agregarCertificacion(Certificacion(true,5000))
          vendedorFijo.agregarCertificacion(Certificacion(false,2000))
          vendedorFijo.agregarCertificacion(Certificacion(false,2000))
          vendedorFijo.esFirme().shouldBeTrue()
      }
      it("no es firme el vendedorFijo2"){
          vendedorFijo2.esFirme().shouldBeFalse()
      }

      it("es versatil"){
        vendedorFijo.agregarCertificacion(Certificacion(true,5000))
        vendedorFijo.agregarCertificacion(Certificacion(true,5000))
        vendedorFijo.agregarCertificacion(Certificacion(false,2000))

        vendedorFijo.esVersatil().shouldBeTrue()
      }

      it("no es versatil el vendedorFijo2"){
          vendedorFijo2.esVersatil().shouldBeFalse()
      }
    }
  }

  describe("Viajante") {

      val viajante = Viajante(listOf(misiones,buenosAires,cordoba))
      val viajante2 = Viajante(listOf(cordoba))
      val viajante3 = Viajante(listOf(misiones))

    describe("puedeTrabajarEn") {
      it("una ciudad que pertenece a una provincia habilitada") {
        viajante.puedeTrabajarEn(hurlingham).shouldBeTrue()
      }
      it("una ciudad que no pertenece a una provincia habilitada") {
        viajante.puedeTrabajarEn(guaymallen).shouldBeFalse()
      }
    }
    describe("es un trabajador"){
      it("influyente porq la poblacion supera 10000000"){
          viajante.esInfluyente().shouldBeTrue()
      }
      it("el vendedor 2 no es influyente porque la poblacion donde esta no supera 10000000 "){
          viajante2.esInfluyente().shouldBeFalse()
      }
    }

    describe("es versatil"){
      viajante.agregarCertificacion(certificacionProducto)
      viajante.agregarCertificacion(certificacionProducto)
      viajante.agregarCertificacion(certificacionDeNoProducto)
      it("es versatil el vendedor viajante"){
        viajante.esVersatil().shouldBeTrue()
      }
      it("tiene al menos 1 certificacion de producto"){
        viajante.esVersatil().shouldBeTrue()
      }
      it("el viajante 3 no es versatil"){
        viajante3.esVersatil().shouldBeFalse()
      }
      it("los puntos que tiene son 130"){
        (viajante.puntajeCertificaciones() == 130).shouldBeTrue()
      }
      it("tiene 2 certificaciones de productos"){
        (viajante.certificacionesDeProducto() == 2).shouldBeTrue()
      }
      it("tiene una certificacion de no producto"){
        (viajante.otrasCertificaciones() == 1).shouldBeTrue()
      }
      it("no tiene 4 certificaciones de producto"){
        (viajante.certificacionesDeProducto() == 4).shouldBeFalse()
      }
      it("no tiene 3 certificaciones de no producto"){
        (viajante.otrasCertificaciones() == 3).shouldBeFalse()
      }
    }
  }

  describe("ComercioCorresponsal"){
    val buenosAires = Provincia(4000000)
    val hurlingham = Ciudad(buenosAires)
    val moron = Ciudad(buenosAires)
    val comercio = ComercioCorresponsal(listOf(hurlingham,sanIgnacio,moron))

    describe("puede trabajar en"){
      it("la ciudad en hurlingham"){
        comercio.puedeTrabajarEn(hurlingham).shouldBeTrue()
      }
      it("no en san ignasio"){
        comercio.puedeTrabajarEn(villaDolores).shouldBeFalse()
      }
    }
    describe("caso de ser influyentes "){
      val obera = Ciudad(misiones)

      val nuevoComer = ComercioCorresponsal(listOf(hurlingham,sanIgnacio,moron,villaDolores,obera))
      val nuevoComer2 = ComercioCorresponsal(listOf(hurlingham,sanIgnacio,moron,villaDolores))

      it("no ser influyente porq tiene 3 ciudades"){
        comercio.esInfluyente().shouldBeFalse()
      }
      it("el nuevo comercio es influyente porque tiene 3 provincias"){

        nuevoComer2.esInfluyente().shouldBeTrue()
      }
      it("el nuevo comercio es influyente porque tiene 5 ciudades"){


        nuevoComer.esInfluyente().shouldBeTrue()
      }
      it("que las provincias donde esta son 2 por ende no es influyente "){
        (comercio.provinciasDeLaCiudadesDondeTieneSucursales().size ==2).shouldBeTrue()
      }
    }
    describe("caso de es firme"){
      comercio.agregarCertificacion(certificacionProducto)
      comercio.agregarCertificacion(certificacionProducto)
      comercio.agregarCertificacion(certificacionDeNoProducto)
      comercio.agregarCertificacion(certificacionDeNoProducto)

      it("tiene un puntaje de certificacion mayor a 30"){
        comercio.esFirme().shouldBeTrue()
      }
      it("tiene 2 certificaciones de producto"){
        (comercio.certificacionesDeProducto() == 2).shouldBeTrue()
      }
      it("no tiene 4 certificaciones de producto"){
        (comercio.certificacionesDeProducto() == 4).shouldBeFalse()
      }
      it("tiene 2 certificaciones de no producto"){
        (comercio.otrasCertificaciones() == 2).shouldBeTrue()
      }
      it("no tiene 3 certificaciones de no producto"){
        (comercio.otrasCertificaciones() == 3).shouldBeFalse()
      }
    }
  }
})
