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
    describe("puede que"){
      it("no es influyente porque la poblacion de las provincias no llega a 10M"){
        viajante.esInfluyente().shouldBeFalse()
      }
      it("el nuevo viajante es influyente porque la poblacion supera 10M"){
        val buenosAires = Provincia(8000000)
        val viajanteNuevo = Viajante(listOf(misiones,cordoba,buenosAires))
        viajanteNuevo.esInfluyente().shouldBeTrue()
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
        comercio.puedeTrabajarEn(sanIgnacio).shouldBeFalse()
      }
    }
    describe("puede "){
      it("no ser influyente"){
        comercio.esInfluyente().shouldBeFalse()
      }
      it("el nuevo comercio es influyente"){
        val nuevoComer = ComercioCorresponsal(listOf(hurlingham,sanIgnacio,moron,villaDolores))
        nuevoComer.esInfluyente().shouldBeTrue()
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
        it("no se agregue un vendedor y rompe") {
          shouldThrowAny { centro.agregarUnVendedor(viajante) }
        }
        it("no es robusto el centro"){
          centro.esRobusto().shouldBeFalse()
        }
        it("al repartir certificaciones se vuelve robusto"){
          centro.agregarUnVendedor(viajanteNuevo)
          centro.repartirCertificaciones(Certificacion(true,500))

          centro.esRobusto().shouldBeTrue()
        }
      it("es firme el vendedor estrella"){
        centro.repartirCertificaciones(Certificacion(true,500))
        val estrella = centro.vendedorEstrella()
        if (estrella != null) {
          estrella.esFirme().shouldBeTrue()
        }
      }
    }
  }
})
