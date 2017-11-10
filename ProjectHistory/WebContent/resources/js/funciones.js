/*VALIDACIONES*/
$(document).ready(function(){
	//Se asignan las validaciones a los campos "moneda" y "numero"
	asignarValidacionesNumero();
	asignarValidacionesMoneda();
	//Otros
	estilizarHorario();
	beforeUpdate();
});

function asignarValidacionesNumero(){
	$('.numero').keypress(validarKeyPressNumero);
	$('.numero').trigger( 'change' ).val('');
}

//validar textos moneda
function asignarValidacionesMoneda(){
	
	$('.moneda').keypress(validarKeyPressNumero);
	$('.moneda').change(function( e ){$( this ).val( formatoMoneda( $(this).val() ) );});
	$('.moneda').keyup(function( e ){$( this ).val( formatoMoneda( $(this).val() ) );});
	$('.moneda').trigger( 'change' ).val('');
	
	$( ".labelMoneda" ).each(function( index ) {
		$( this ).text( formatoMoneda( $(this).text() ) )
	});
	
}

//formato moneda
var formatoMoneda = function(num){
	var str = num.toString().replace("$", ""), parts = false, output = [], i = 1, formatted = null;
	if(str.indexOf("") > 0) {
		parts = str.split(",");
		str = parts[0];
	}
	str = str.split("").reverse();
	for(var j = 0, len = str.length; j < len; j++) {
		if(str[j] != ",") {
			output.push(str[j]);
			if(i%3 == 0 && j < (len - 1)) {
				output.push(",");
			}
			i++;
		}
	}
	formatted = output.reverse().join("");
//	return("$" + formatted + ((parts) ? "." + parts[1].substr(0, 2) : ""));
	return(formatted + ((parts) ? "." + parts[1].substr(0, 2) : ""));
};

//Validar cajas numericas
function validarKeyPressNumero(event){
	var charCode = (event.which) ? event.which : event.keyCode; 
	/**
	 * CharCode 48 = 0
	 * CharCode 57 = 9
	 * CharCode 8 = BackSpace
	 * CharCode 9 = Tab
	 * CharCode 49 = Supr
	 */
	if(charCode < 48 || charCode > 57) {
		if(charCode!=8 && charCode!=9 && charCode!=49)
			return false;
	}
}

function noCopyCutPaste(data) {
	var myInput = data;
	myInput.onpaste = function(e) {
		e.preventDefault();
	}
	myInput.oncopy = function(e) {
		e.preventDefault();
	}
	myInput.oncut = function(e) {
		e.preventDefault();
	}
}

function igualarAltoItems(objeto){
    var maxHeight = 150;
    //CICLO IMG
    for (var i = objeto.length - 1; i >= 0; i--) {
        var obj = $(objeto[i]);
        if(obj.height()>maxHeight){
            maxHeight = obj.height();
        }
    };
    maxHeight = maxHeight+1;
    //ASIGNAR HEIGHT A LOS DEMÁS ELEMENTOS
    objeto.css("min-height",maxHeight);
}

function ocultarPrimeraFilaTabla(){
//	setTimeout(function(){
		$("#form\\:fila-tabla-sin-menu .row").first().hide();		
//	}, 500);
}

function aplicarColorHorario(){
	setTimeout(function(){
		if($(".fc-event-container > a").length > 0){
			
			$(".fc-event-container > a").each(function(){
				var clases = $(this).attr('class').split(' ');
				for (var i = 0; i < clases.length; i++) {
					var clase = clases[i];
					if(clase.match(/clase-.*/)){
						var color = clase.split('-')[1];
						$('.'+clase).css("background", '#'+color);
						$('.'+clase).css("border", "none");
					}
				}
			});			
		}
	},500);
}

function botonesHorario(){
	$(".fc-toolbar").find("button").each(function(){
		$(this).click(function(){
			aplicarColorHorario();
		})
	});	
}

function estilizarHorario(){
	$("div.fc-time").prepend('<i class="fa fa-clock-o" aria-hidden="true" style="margin:0 3px 0 3px" />');
}

function beforeUpdate(){
	$('.ui-datalist-content.ui-widget-content').addClass('row');
	$('.ui-paginator.ui-paginator-bottom.ui-widget-header.ui-corner-bottom').addClass('row');
	igualarAltoItems($(".thumbnail"));
	igualarAltoItems($(".thumbnail").parent());
//	igualarAltoItems($(".caption"));
	try {
		if (PrimeFaces.locales['es'] === undefined) {
			PrimeFaces.locales['es'] = {
			  closeText: 'Cerrar',
			  prevText: 'atras',
			  nextText: 'siguiente',
			  currentText: 'Actual',
			  monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio',
			      'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'],
			  monthNamesShort: ['Ene','Feb','Mar','Abr','May','Jun',
			      'Jul','Ago','Sep','Oct','Nov','Dic'],
			  dayNames: ['Domingo', 'Lunes','Martes','Miercoles','Jueves','Viernes','Sabado'],
			  dayNamesShort: ['Dom','Lun','Mar','Mie','Jue','Vie','Sab'],
			  dayNamesMin: ['Do','Lu','Ma','Mi','Ju','Vi','Sa'],
			  weekHeader: 'Sem',
			  firstDay: 1,
			  isRTL: false,
			  showMonthAfterYear: false,
			  yearSuffix: '',
			  month: 'Mes',
			  week: 'Semana',
			  day: 'Día',
			  allDayText : 'Todo el día'
			};
		} else {
			console.log('no-object-primefaces');
		}
	} catch (e) {
		
	}		
}
