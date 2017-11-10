package com.oldschool.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Fecha {
	
	public static final String FORMATO_yyyy_MM_dd = "yyyy-MM-dd";
	public static final String FORMATO_dd_MM_yyyy = "dd-MM-yyyy";
	
	public static final String NOMBRE_ENERO = "Enero";
	public static final String NOMBRE_FEBRERO = "Febrero";
	public static final String NOMBRE_MARZO = "Marzo";
	public static final String NOMBRE_ABRIL = "Abril";
	public static final String NOMBRE_MAYO = "Mayo";
	public static final String NOMBRE_JUNIO = "Junio";
	public static final String NOMBRE_JULIO = "Julio";
	public static final String NOMBRE_AGOSTO = "Agosto";
	public static final String NOMBRE_SEPTIEMBRE = "Septiembre";
	public static final String NOMBRE_OCTUBRE = "Octubre";
	public static final String NOMBRE_NOVIEMBRE = "Noviembre";
	public static final String NOMBRE_DICIEMBRE = "Diciembre";
	
	//VARIABLES
	private int dia;
	private int diaSemana;
	private int mes;
	private int anio;
	private int hora;
	private int minuto;
	private int segundo;
	private String nombreDiaSemana;
	
	private Date fechaDate;
	private Calendar fechaCalendar;
	
	public Fecha(int dia, int mes, int anio) {
		setFechaDate(construirDate(dia, mes, anio, 0, 0, 0));
	}
	
	public Fecha(Date date) {
		setFechaDate(date);
	}
	
	public Fecha(Time time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		setFechaCalendar(calendar);
	}

	public Fecha(){
		setFechaDate(new Date());
	}
	
	/*METODOS*/
	/**Este método compara la fecha dada
	 * @param fecha Fecha de comparación
	 * @return 0 En caso de ser igual.  
	 * -1 En caso de que la fecha actual sea menor a la indicada por parametro. 
	 * 1 En caso de que la fecha actual sea mayor a la indicada por parametro.
	 * */
	public int comparar(Fecha fecha) {
		if (this.equals(fecha, true)) {
			return 0;
		} else if (this.esMenorQue(fecha)) {
			return -1;
		} else {
			return 1;
		}
	}
	
	public boolean esMenorQue(Fecha fecha){
		if (fecha.getFechaCalendar().after(getFechaCalendar())) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean esMayorQue(Fecha fecha) {
		if (fecha.getFechaCalendar().before(getFechaCalendar())) {
			return true;
		} else {
			return false;
		}
	}
	
	public void diferenciaDias(Fecha fecha){
		intervaloDeDias(this.getFechaDate(), fecha.getFechaDate(), true);
	}
	
	public int diferenciaDiasHabiles(Fecha fecha){
		int cont = 0;
		Fecha temp = this;
		while(fecha.esMayorQue(temp)){
			if(temp.esDiaHabil()){
				temp.aumentarDias(1);
				cont++;
			}
		}
		return cont;
	}
	
	public boolean esDiaHabil() {
		if ((getDiaSemana() == Calendar.SUNDAY) || (getDiaSemana() == Calendar.SATURDAY) || (esFestivoColombia())) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean esFestivoColombia(){
		
		Fecha primeroEnero = new Fecha(1, Calendar.JANUARY+1, getAnio());
		Fecha diaTrabajo = new Fecha(1, Calendar.MAY+1, getAnio());
		Fecha independencia = new Fecha(20, Calendar.JULY+1, getAnio());
		Fecha batallaBoyaca = new Fecha(7, Calendar.AUGUST+1, getAnio());
		Fecha mariaInmaculada = new Fecha(8, Calendar.DECEMBER+1, getAnio());
		Fecha navidad = new Fecha(25, Calendar.DECEMBER+1, getAnio());

		// Festivos que pasan al lunes
		Fecha reyesMagos = new Fecha(6, Calendar.JANUARY+1, getAnio());
		reyesMagos.aumentarDiasHastaSiguienteDiaSemana(Calendar.MONDAY-1);

		Fecha sanJose = new Fecha(19, Calendar.MARCH+1, getAnio());
		sanJose.aumentarDiasHastaSiguienteDiaSemana(Calendar.MONDAY-1);

		Fecha sanPedro = new Fecha(29, Calendar.JUNE+1, getAnio());
		sanPedro.aumentarDiasHastaSiguienteDiaSemana(Calendar.MONDAY-1);

		Fecha asuncion = new Fecha(15, Calendar.AUGUST+1, getAnio());
		asuncion.aumentarDiasHastaSiguienteDiaSemana(Calendar.MONDAY-1);

		Fecha descubrimientoAmerica = new Fecha(12, Calendar.OCTOBER+1, getAnio());
		descubrimientoAmerica.aumentarDiasHastaSiguienteDiaSemana(Calendar.MONDAY-1);

		Fecha todosLosSantos = new Fecha(1, Calendar.NOVEMBER+1, getAnio());
		todosLosSantos.aumentarDiasHastaSiguienteDiaSemana(Calendar.MONDAY-1);

		Fecha independenciaCaragena = new Fecha(11, Calendar.NOVEMBER+1, getAnio());
		independenciaCaragena.aumentarDiasHastaSiguienteDiaSemana(Calendar.MONDAY-1);

		Fecha fechaPascua1 = fechaPascua();
		Fecha juevesSanto = fechaPascua1; // Jueves Santo – Día de
		juevesSanto.disminuirDias(3);

		Fecha viernesSanto = fechaPascua1; // Viernes Santo – Día
		viernesSanto.disminuirDias(2);

		Fecha ascencionSenor = fechaPascua1;// Ascención del Señor,
		ascencionSenor.aumentarDias(39);
		ascencionSenor.aumentarDiasHastaSiguienteDiaSemana(Calendar.MONDAY);

		Fecha corpusCristi = fechaPascua1;// Corpus Cristi, día de
		corpusCristi.aumentarDias(60);
		corpusCristi.aumentarDiasHastaSiguienteDiaSemana(Calendar.MONDAY);

		Fecha sagradoCorazon = fechaPascua1;// Sagrado Corazón, día
		sagradoCorazon.aumentarDias(68);
		sagradoCorazon.aumentarDiasHastaSiguienteDiaSemana(Calendar.MONDAY);
		
		if ((equals(primeroEnero, false))
				|| (equals(diaTrabajo, false))
				|| (equals(independencia, false))
				|| (equals(batallaBoyaca, false))
				|| (equals(mariaInmaculada, false))
				|| (equals(navidad, false))
				|| (equals(reyesMagos, false))
				|| (equals(sanJose, false))
				|| (equals(sanPedro, false))
				|| (equals(asuncion, false))
				|| (equals(descubrimientoAmerica, false))
				|| (equals(todosLosSantos, false))
				|| (equals(independenciaCaragena, false))
				|| (equals(juevesSanto, false))
				|| (equals(viernesSanto, false))
				|| (equals(ascencionSenor, false))
				|| (equals(corpusCristi, false))
				|| (equals(sagradoCorazon, false))
				|| (equals(independencia, false))) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean equals(Fecha fecha, boolean tiempo){
		if(tiempo){
			if (this.dia == fecha.getDia() 
					&& this.mes == fecha.getMes() 
					&& this.anio == fecha.getAnio()
					&& this.hora == fecha.getHora()
					&& this.minuto == fecha.getMinuto()) {
				return true;
			}else{
				return false;
			}
		}else{
			if (this.dia == fecha.getDia() 
					&& this.mes == fecha.getMes() 
					&& this.anio == fecha.getAnio()) {
				return true;
			}else{
				return false;
			}
		}
	}
	
	private void aumentarDiasHastaSiguienteDiaSemana(int diasSemana) {
		while (getDiaSemana() != diasSemana) {
			aumentarDias(1);
		}
	}
	
	public void aumentarDias(int diasAu) {
		try {
			fechaCalendar.add(Calendar.DAY_OF_MONTH, diasAu);
			setFechaCalendar(fechaCalendar);
		} catch (Exception e) {
			System.out.println("Clase Fecha:aumentarDias " + e);
		}
	}
	
	public void disminuirDias(int diasAu) {
		try {
			fechaCalendar.add(Calendar.DAY_OF_MONTH, (diasAu * -1));
			setFechaCalendar(fechaCalendar);
		} catch (Exception e) {
			System.out.println("Clase Fecha:disminuirDias " + e);
		}
	}
	
	public String toString() {
		try {
			SimpleDateFormat formato = new SimpleDateFormat(FORMATO_dd_MM_yyyy);
			return formato.format(fechaDate);
		} catch (Exception e) {
			System.out.println("Fechas:toString " + e);
			return null;
		}

	}

	public String toString(String fmt) {
		try {
			SimpleDateFormat formato = new SimpleDateFormat(fmt);
			return formato.format(fechaDate);
		} catch (Exception e) {
			System.out.println("Fechas:toString " + e);
			return null;
		}

	}
	
	public static String toString(Date date) {
		try {
			SimpleDateFormat formato = new SimpleDateFormat(FORMATO_dd_MM_yyyy);
			return formato.format(date);
		} catch (Exception e) {
			System.out.println("Fechas:toString " + e);
			return null;
		}

	}
	
	public static String toString(Date date, String fmt) {
		try {
			SimpleDateFormat formato = new SimpleDateFormat(fmt);
			return formato.format(date);
		} catch (Exception e) {
			System.out.println("Fecha:toString " + e);
			return null;
		}
	}
	
	public static Date calendarToDate(Calendar myCalendar) {
		return myCalendar.getTime();
	}
	
	/*CALCULAR FESTIVOS ESPECIALES*/
	public Fecha fechaPascua() {
		int a, b, c, d, e, diaPas, mesPas;
		a = getAnio() % 19;
		b = getAnio() % 4;
		c = getAnio() % 7;
		d = (19 * a + 24) % 30;
		e = (2 * b + 4 * c + 6 * d + 5) % 7;
		diaPas = 22 + d + e;

		if (diaPas <= 31) {
			mesPas = 3;
		} else {
			mesPas = 4;
			diaPas -= 31;
		}

		return new Fecha(diaPas, mesPas, getAnio());
	}
	
	/*MÉTODOS ESTATICOS*/
	/**Este método valida si la fecha especificada tiene el formato deseado
	 * @param fecha Fecha a comparar
	 * @param formato Formato de fecha*/
	public static boolean esFechaValida(String fecha, String formato) {
		boolean respuesta;
		try {
			SimpleDateFormat formatoFecha = new SimpleDateFormat(formato, Locale.getDefault());
			formatoFecha.setLenient(false);
			formatoFecha.parse(fecha);
			respuesta = true;
		} catch (ParseException e) {
			respuesta = false;
		}
		return respuesta;
	}
	
	public static Date quitarTiempo(Date date) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		date.setTime(cal.getTime().getTime());
		return date;
	}
	
	public static Date agregarTiempo(Date date, int hora, int minutos, int segundos) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hora);
		cal.set(Calendar.MINUTE, minutos);
		cal.set(Calendar.SECOND, segundos);
		cal.set(Calendar.MILLISECOND, 0);
		date.setTime(cal.getTime().getTime());
		return date;
	}
	
	/**Este método calcula cuantos días hay entre dos fechas
	 * @param fechaIni Fecha inicial
	 * @param fechaFin Fecha de fin
	 * @param incluirFechaInicio Si se incluye la fecha de inicio o no (true/false)
	 * @return Intervalo de días 
	 * */
	public static int intervaloDeDias(Date fechaIni, Date fechaFin, boolean incluirFechaInicio) {

		fechaIni = Fecha.quitarTiempo(fechaIni);
		Calendar inicio = Calendar.getInstance();
		inicio.setTime(fechaIni);

		fechaFin = Fecha.quitarTiempo(fechaFin);
		Calendar fin = Calendar.getInstance();
		fin.setTime(fechaFin);

		if (incluirFechaInicio) {
			inicio.add(Calendar.DATE, -1);
		}

		int dias = 0;
		while (inicio.before(fin)) {
			dias++;
			inicio.add(Calendar.DATE, 1);
		}
		return dias;
	}
	
	public static Date construirDate(int day, int month, int year, int hourofday, int minute, int second) {
		if (day == 0 && month == 0 && year == 0) return null;
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day, hourofday, minute, second);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**Los meses empiezan desde el 0 hasta el 11*/
	public static Calendar construirCalendar(int day, int month, int year, int hourofday, int minute, int second) {
		if (day == 0 && month == 0 && year == 0) return null;
		Locale spanish = new Locale("es", "ES");
		Calendar cal = Calendar.getInstance(spanish);
		cal.set(year, month, day, hourofday, minute, second);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	public static long convertirSegAHor(long segundos){
		long horas = Math.round(segundos/3600);
		return horas;
	}
	
	public static int cantidadSemanasPorMes(int mes, int anio){
		Locale spanish = new Locale("es", "ES");
		Calendar cal = Calendar.getInstance(spanish);
		mes = mes-1;
		cal.set(Calendar.YEAR, anio);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, mes);
		int maxWeeknumber = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
	    return maxWeeknumber;
	}
	
	public static Fecha fechaInicialPorNumeroSemana(int numSemana, int anio){
	    
		Locale spanish = new Locale("es", "ES");
		Calendar cal = Calendar.getInstance(spanish);
		cal.set(Calendar.YEAR, anio);
	    cal.set(Calendar.WEEK_OF_YEAR, numSemana);
	    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	    //System.out.println(new SimpleDateFormat(FORMATO_dd_MM_yyyy).format(cal.getTime()));
	    
	    Fecha fechInicial = new Fecha();
	    fechInicial.setFechaCalendar(cal);
	    
	    return fechInicial;
	}
	
	public static Fecha fechaFinalPorNumeroSemana(int numSemana, int anio){
	    
		Locale spanish = new Locale("es", "ES");
		Calendar cal = Calendar.getInstance(spanish);
		cal.set(Calendar.YEAR, anio);
	    cal.set(Calendar.WEEK_OF_YEAR, numSemana);
	    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
	    //System.out.println(new SimpleDateFormat(FORMATO_dd_MM_yyyy).format(cal.getTime()));
	    
	    Fecha fechInicial = new Fecha();
	    fechInicial.setFechaCalendar(cal);
	    
	    return fechInicial;
	}
	
	public static Date obtenerTiempoActual(TimeZone timeZone){
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance(timeZone);
			String calStr = cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH)+1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + " " + 
					cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
			Date fecha = formatter.parse( calStr );
			return fecha;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*GET & SET*/
 	public String getNombreMes(){
		String nombreMes = null;
		switch (this.mes) {
			case 0: nombreMes = NOMBRE_ENERO; break;
			case 1: nombreMes = NOMBRE_FEBRERO; break;
			case 2: nombreMes = NOMBRE_MARZO; break;
			case 3: nombreMes = NOMBRE_ABRIL; break;
			case 4: nombreMes = NOMBRE_MAYO; break;
			case 5: nombreMes = NOMBRE_JUNIO; break;
			case 6: nombreMes = NOMBRE_JULIO; break;
			case 7: nombreMes = NOMBRE_AGOSTO; break;
			case 8: nombreMes = NOMBRE_SEPTIEMBRE; break;
			case 9: nombreMes = NOMBRE_OCTUBRE; break;
			case 10: nombreMes = NOMBRE_NOVIEMBRE; break;
			case 11: nombreMes = NOMBRE_DICIEMBRE; break;
		}
		return nombreMes;
	}
	public int getDia() {
		return dia;
	}
	public void setDia(int dia) {
		this.dia = dia;
	}
	public int getDiaSemana() {
		return diaSemana;
	}
	public void setDiaSemana(int diaSemana) {
		this.diaSemana = diaSemana;
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	public int getAnio() {
		return anio;
	}
	public void setAnio(int anio) {
		this.anio = anio;
	}
	public int getHora() {
		return hora;
	}
	public void setHora(int hora) {
		this.hora = hora;
	}
	public int getMinuto() {
		return minuto;
	}
	public void setMinuto(int minuto) {
		this.minuto = minuto;
	}
	public int getSegundo() {
		return segundo;
	}
	public void setSegundo(int segundo) {
		this.segundo = segundo;
	}
	public Calendar getFechaCalendar() {
		return fechaCalendar;
	}
	public void setFechaCalendar(Calendar fechaCalendar) {
		setFechaDate(calendarToDate(fechaCalendar));
	}
	public String getNombreDiaSemana() {
		switch (diaSemana) {
		case Calendar.SUNDAY:
				nombreDiaSemana = "DOMINGO";
			break;
		case Calendar.MONDAY:
				nombreDiaSemana = "LUNES";
			break;
		case Calendar.TUESDAY:
				nombreDiaSemana = "MARTES";
			break;
		case Calendar.WEDNESDAY:
				nombreDiaSemana = "MIERCOLES";
			break;
		case Calendar.THURSDAY:
				nombreDiaSemana = "JUEVES";
			break;
		case Calendar.FRIDAY:
			nombreDiaSemana = "VIERNES";
		break;
		case Calendar.SATURDAY:
			nombreDiaSemana = "SÁBADO";
		break;

		default:
			break;
		}
		return nombreDiaSemana;
	}
	public void setNombreDiaSemana(String nombreDiaSemana) {
		this.nombreDiaSemana = nombreDiaSemana;
	}
	public Date getFechaDate() {
		return fechaDate;
	}
	public void setFechaDate(Date fechaDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaDate);
		dia = calendar.get(Calendar.DAY_OF_MONTH);
		diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
		mes = calendar.get(Calendar.MONTH);
		anio = calendar.get(Calendar.YEAR);
		hora = calendar.get(Calendar.HOUR_OF_DAY);
		minuto = calendar.get(Calendar.MINUTE);
		segundo = calendar.get(Calendar.SECOND);
		fechaCalendar = calendar;
		this.fechaDate = fechaDate;
	}
	
}
