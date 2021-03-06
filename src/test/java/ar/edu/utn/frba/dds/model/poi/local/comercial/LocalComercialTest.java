package ar.edu.utn.frba.dds.model.poi.local.comercial;

import java.util.HashSet;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.edu.utn.frba.dds.BaseTest;
import ar.edu.utn.frba.dds.model.poi.Geolocalizacion;
import ar.edu.utn.frba.dds.model.poi.horario.RangoHorario;
import ar.edu.utn.frba.dds.model.poi.horario.RangoHorarioEspecial;
import ar.edu.utn.frba.dds.model.poi.local.comercial.LocalComercial;
import ar.edu.utn.frba.dds.model.poi.local.comercial.Rubro;
import ar.edu.utn.frba.dds.util.time.DateTimeProviderImpl;

public class LocalComercialTest extends BaseTest {

    private LocalComercial local;
    private Rubro rubroLibreria;
    private Geolocalizacion geolocalizacionLocal;

    @Before
    public void setUp() throws Exception {
        // setUp para estaDisponible
        LocalTime horaInicioLunesAViernes = new LocalTime(9, 0);
        LocalTime horaFinLunesAViernes = new LocalTime(13, 0);
        LocalTime horaInicioLunesAViernes2 = new LocalTime(15, 0);
        LocalTime horaFinLunesAViernes2 = new LocalTime(18, 30);
        LocalTime horaInicioSabado = new LocalTime(10, 0);
        LocalTime horaFinSabado = new LocalTime(13, 30);
        local = new LocalComercial(new DateTimeProviderImpl(new DateTime(2016, 05, 20, 13, 30, 0)));
        local.agregarRangoHorario(new RangoHorario(1, horaInicioLunesAViernes, horaFinLunesAViernes));
        local.agregarRangoHorario(new RangoHorario(2, horaInicioLunesAViernes, horaFinLunesAViernes));
        local.agregarRangoHorario(new RangoHorario(3, horaInicioLunesAViernes, horaFinLunesAViernes));
        local.agregarRangoHorario(new RangoHorario(4, horaInicioLunesAViernes, horaFinLunesAViernes));
        local.agregarRangoHorario(new RangoHorario(5, horaInicioLunesAViernes, horaFinLunesAViernes));
        local.agregarRangoHorario(new RangoHorario(1, horaInicioLunesAViernes2, horaFinLunesAViernes2));
        local.agregarRangoHorario(new RangoHorario(2, horaInicioLunesAViernes2, horaFinLunesAViernes2));
		local.agregarRangoHorario(new RangoHorario(3, horaInicioLunesAViernes2, horaFinLunesAViernes2));
		local.agregarRangoHorario(new RangoHorario(4, horaInicioLunesAViernes2, horaFinLunesAViernes2));
		local.agregarRangoHorario(new RangoHorario(5, horaInicioLunesAViernes2, horaFinLunesAViernes2));
		local.agregarRangoHorario(new RangoHorario(6, horaInicioSabado, horaFinSabado));
		local.agregarRangoHorario(new RangoHorarioEspecial(new LocalDate(2016, 12, 25), horaInicioSabado, horaFinSabado, true));
		local.agregarRangoHorario(new RangoHorarioEspecial(new LocalDate(2016, 12, 24), horaInicioSabado, horaFinSabado, false));
        // setUp para esCercano
        rubroLibreria = new Rubro();
        geolocalizacionLocal = new Geolocalizacion(12, 28);
        rubroLibreria.setNombre("Libreria Escolar");
        rubroLibreria.setRadioCercania(5);
        
        
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void siendoUnaFechadentroDelHorarioDeAtencionDeUnLocalDebeEstarDisponible() {
    	local.setDateTimeProvider(new DateTimeProviderImpl(new DateTime(2016, 05, 20, 12, 00, 0)));
        Assert.assertTrue(this.local.estaDisponible());
    }

    @Test
    public void siendoUnaFechaFueraDelHorarioDeAtencionDeUnLocalNoDebeEstarDisponible() {
    	local.setDateTimeProvider(new DateTimeProviderImpl(new DateTime(2016, 11, 17, 20, 30, 0)));
        Assert.assertFalse(this.local.estaDisponible());
    }
    
    @Test
    public void siendoUnaFechaFueraDelHorarioDeAtencionPeroDentroDelHorarioEspecialDebeEstarDisponible(){
    	local.setDateTimeProvider(new DateTimeProviderImpl(new DateTime(2016, 12, 25, 11, 30, 0)));
    	Assert.assertTrue(this.local.estaDisponible());
    }
    
    @Test
    public void siendoUnaFechaDentroDelHorarioNormalIgualNoAtiendePorExistirUnHorarioEspecialCerrado(){
    	local.setDateTimeProvider(new DateTimeProviderImpl(new DateTime(2016, 12, 24, 11, 30, 0)));
    	local.getHorariosEspeciales().forEach(x -> System.out.println("Especial:"+x.getFecha() +" inicio:"+x.getHoraInicio() + " fin:"+x.getHoraFin() + " abierto:"+x.isAbierto()));
        Assert.assertFalse(this.local.estaDisponible());
    }
    
    // Da alrededor de 3000 cuadras de distancia. No es Cercano.
    @Test
    public void dadaUnaGeolocalizacionFueraDelRangoLibreriaEscolarNoDebeSerCercana() {
        local.setGeolocalizacion(geolocalizacionLocal);
        local.setRubro(rubroLibreria);
        Geolocalizacion unaGeolocalizacion = new Geolocalizacion(11, 30);
        Assert.assertFalse(local.esCercano(unaGeolocalizacion));
    }

    @Test
    public void dadaUnaGeolocalizacionDentroDelRangoLibreriaEscolarDebeSerCercana() {
        local.setGeolocalizacion(geolocalizacionLocal);
        local.setRubro(rubroLibreria);
        Geolocalizacion unaGeolocalizacion = new Geolocalizacion(11.999991, 28.000001);
        Assert.assertTrue(local.esCercano(unaGeolocalizacion));
    }

    @Test
    public void dadaUnaPalabraLibreriaEscolarTienePalabraDebeCoincidirConNombre() {
        local.setNombre("Reglas y comp??s");
        local.setRubro(rubroLibreria);
        HashSet<String> palabrasClave = new HashSet<String>();
        local.setPalabrasClave(palabrasClave);
        Assert.assertTrue(local.tienePalabra("reGlas"));
    }

    @Test
    public void dadaUnaPalabraContenidaEnElNombreDelRubroLibreriaEscolarDebeTenerLaPalabra() {
        local.setNombre("Reglas y comp??s");
        local.setRubro(rubroLibreria);
        HashSet<String> palabrasClave = new HashSet<String>();
        local.setPalabrasClave(palabrasClave);
        Assert.assertTrue(local.tienePalabra("breRIA"));
    }

    @Test
    public void dadaUnaPalabraQueNoEsClaveNiDelRubroNiDelNombreLibreriaEscolarNoDebeTenerEsaPalabra() {
        local.setNombre("Reglas y comp??s");
        local.setRubro(rubroLibreria);
        HashSet<String> palabrasClave = new HashSet<String>();
        local.setPalabrasClave(palabrasClave);
        Assert.assertFalse(local.tienePalabra("futbol"));
    }
    
    

}
