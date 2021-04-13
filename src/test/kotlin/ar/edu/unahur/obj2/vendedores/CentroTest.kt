package ar.edu.unahur.obj2.vendedores

import ar.edu.unahur.obj2.vendedores.*
import io.kotest.assertions.throwables.shouldThrowAny

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class CentroTest :  DescribeSpec({

    val misiones = Provincia(1300000)
    val sanIgnacio = Ciudad(misiones)
    val cordoba = Provincia(2000000)
    val villaDolores = Ciudad(cordoba)

    val viajante = Viajante(listOf(misiones))
    val viajante2 = Viajante(listOf(cordoba))
    val viajanteNuevo = Viajante(listOf(misiones, cordoba))

    val obera = Ciudad(misiones)
    val vendedorFijo = VendedorFijo(obera)

    vendedorFijo.agregarCertificacion(Certificacion(false, 50))
    vendedorFijo.agregarCertificacion(Certificacion(false, 50))

    val vendedoresDeCentro = mutableListOf<Vendedor>()
    vendedoresDeCentro.add(viajante)
    vendedoresDeCentro.add(viajante2)
    vendedoresDeCentro.add(vendedorFijo)

    val centro = CentroDeDistribucion(villaDolores, vendedoresDeCentro)

    describe("puedeQue") {
        it("los vendedores que tiene el centro son 3") {
            (centro.vendedores.size == 3).shouldBeTrue()
        }
        it("se agrega un vendedor y ahora el centro tiene 4") {
            centro.agregarUnVendedor(viajanteNuevo)
            (centro.vendedores.size == 4).shouldBeTrue()
        }
        it("no se agregue un vendedor y rompe") {
            shouldThrowAny { centro.agregarUnVendedor(viajante) }
        }

    }
    describe("caso de vendedor estrella") {
        it("el vendedor estrella es el vendedorFijo") {
            (centro.vendedorEstrella() == vendedorFijo).shouldBeTrue()
        }

        it("el vendedor estrella no es el viajante") {
            (centro.vendedorEstrella() == viajante).shouldBeFalse()
        }
    }
    describe("caso de si es robusto") {
        it("no es robusto el centro") {
            centro.esRobusto().shouldBeFalse()
        }
        it("al repartir certificaciones se vuelve robusto") {

            centro.repartirCertificaciones(Certificacion(true, 50))

            centro.esRobusto().shouldBeTrue()
        }
    }
    describe("caso vendedor estrella firme") {
        it("es firme el vendedor estrella") {
            centro.repartirCertificaciones(Certificacion(true, 500))
            val estrella = centro.vendedorEstrella()
            if (estrella != null) {
                estrella.esFirme().shouldBeTrue()
            }
        }
        it("no es firme el vendedor estrella") {
            val estrella = centro.vendedorEstrella()
            if (estrella != null) {
                estrella.esFirme().shouldBeTrue()
            }
        }
    }
    describe("caso en genericos") {
        it("el vendedorFijo es un vendedor generico") {
            (centro.vendedoresGenericos() == listOf(vendedorFijo)).shouldBeTrue()
        }
        it("el viajante no es un vendedor generico") {
            (centro.vendedoresGenericos() == listOf(viajante)).shouldBeFalse()
        }
    }
})