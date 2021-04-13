package ar.edu.unahur.obj2.vendedores

class Certificacion(val esDeProducto: Boolean, val puntaje: Int)

abstract class Vendedor {
    // Acá es obligatorio poner el tipo de la lista, porque como está vacía no lo puede inferir.
    // Además, a una MutableList se le pueden agregar elementos
    val certificaciones = mutableListOf<Certificacion>()

    // Definimos el método abstracto.
    // Como no vamos a implementarlo acá, es necesario explicitar qué devuelve.
    abstract fun puedeTrabajarEn(ciudad: Ciudad): Boolean

    // En las funciones declaradas con = no es necesario explicitar el tipo
    fun esVersatil() =
        certificaciones.size >= 3
                && this.certificacionesDeProducto() >= 1
                && this.otrasCertificaciones() >= 1

    // Si el tipo no está declarado y la función no devuelve nada, se asume Unit (es decir, vacío)
    fun agregarCertificacion(certificacion: Certificacion) {
        certificaciones.add(certificacion)
    }

    fun esFirme() = this.puntajeCertificaciones() >= 30

    fun certificacionesDeProducto() = certificaciones.count { it.esDeProducto }
    fun otrasCertificaciones() = certificaciones.count { !it.esDeProducto }

    fun puntajeCertificaciones() = certificaciones.sumBy { c -> c.puntaje }

    //etapa 2
    abstract fun esInfluyente(): Boolean
}

// En los parámetros, es obligatorio poner el tipo
class VendedorFijo(val ciudadOrigen: Ciudad) : Vendedor() {

    override fun puedeTrabajarEn(ciudad: Ciudad): Boolean {
        return ciudad == ciudadOrigen
    }

    override fun esInfluyente(): Boolean {
        return false
    }
}

// A este tipo de List no se le pueden agregar elementos una vez definida
class Viajante(val provinciasHabilitadas: List<Provincia>) : Vendedor() {
    override fun puedeTrabajarEn(ciudad: Ciudad): Boolean {
        return provinciasHabilitadas.contains(ciudad.provincia)
    }

    override fun esInfluyente() = this.provinciaHabilitadasDelViajante().sumBy{it.poblacion} >= 10000000

    fun provinciaHabilitadasDelViajante() = provinciasHabilitadas.toSet()
}

class ComercioCorresponsal(val ciudades: List<Ciudad>) : Vendedor() {
    override fun puedeTrabajarEn(ciudad: Ciudad): Boolean {
        return ciudades.contains(ciudad)
    }

    override fun esInfluyente(): Boolean {
        return ciudades.size >= 5 || this.provinciasDeLaCiudadesDondeTieneSucursales().size >= 3
    }

    fun provinciasDeLaCiudadesDondeTieneSucursales() = ciudades.map{it.provincia}.toSet()
}

class CentroDeDistribucion(val ciudadDondeEsta: Ciudad, val vendedores: MutableList<Vendedor> = mutableListOf<Vendedor>()) {


    fun agregarUnVendedor(vendedorAAgregrar: Vendedor) {
        /*

        NOTA: deje comentada la forma original en la que habia planteado la resolucion en este punto

        var respuestaDeAgregar = "se agrego perfectamente"

        if (!vendedores.contains(vendedorAAgregrar)){
            vendedores.add(vendedorAAgregrar)
        }else{
            respuestaDeAgregar ="no se pudo agregar"
        }
        return respuestaDeAgregar
        */

        check(!vendedores.contains(vendedorAAgregrar)){
            "no se agrego al vendedor"
        }
        vendedores.add(vendedorAAgregrar)
    }


    fun vendedorEstrella() = vendedores.maxBy{ it.puntajeCertificaciones()}


    fun puedeCubrirLaCiudad_(ciudad: Ciudad) : Boolean{
        return vendedores.any{it.puedeTrabajarEn(ciudad)}
    }

    fun vendedoresGenericos() = vendedores.filter{ it.otrasCertificaciones() > 1}

    fun esRobusto() = vendedores.count{it.esFirme()} > 3

    fun repartirCertificaciones(certificacionDada: Certificacion){
        vendedores.forEach { it.agregarCertificacion(certificacionDada) }

    }
}

