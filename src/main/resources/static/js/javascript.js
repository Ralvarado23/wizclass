/*Ajuste para la seccion de perfil del menu*/
function adjustUserDropdown() {
  	let screenWidth = $(window ).width();
	let dropdownUser = $("#dropdownUser");
	if (screenWidth>=992) {
		dropdownUser.removeClass( "dropdown-menu-left" ).addClass("dropdown-menu-right");
	}else{
		dropdownUser.removeClass( "dropdown-menu-right" ).addClass("dropdown-menu-left");
	}
}

/*Ajuste para la imagen de la seccion de contenidos del index*/
function adjustImageContentSection() {
	let textContentSection = $("#textContentSection");
	let width = textContentSection.width();
	let height = textContentSection.height();

	if (screen.width>=768) {
		let imageContentSection = $("#imageContentSection");
		imageContentSection.css('max-height', height+20);
	}
}

/*Ajuste video noticia tablets pequeñas y móviles*/
function adjustVid() {
	let video = document.getElementById('youtubeVid');

	if (screen.width<768) {
		if (video != null) {
			video.style.width = "100%";
			let videoWidth = video.offsetWidth;
			let videoHeight = (315/560)*videoWidth;
			video.height = videoHeight;
		}
	}else{
		if (video != null) {
			video.style.width = "";
			video.width = "560";
			video.height = "315";
		}
	}
}

/*Ajuste para la altura del main*/
function adjustMainHeight() {
	let main = document.getElementsByTagName("main")[0];
	let screenHeight = $(window).height();
	main.style.minHeight = screenHeight*0.65 + "px";
}

/*Ajuste main para footer bottom*/
function adjustFooterBottom() {
	let main = document.getElementsByTagName("main")[0];
	let margenMain = document.getElementsByTagName("footer")[0].clientHeight;
	if (margenMain<100) {
		main.style.marginBottom = margenMain + 20 + 'px';
	}else{
		main.style.marginBottom = margenMain + 70 + 'px';
	}
}

/*Encriptar email de contacto de forma segura*/
function encryptMail() {
	var emailriddlerarray=[99,111,110,116,97,99,116,111,64,119,105,122,99,108,97,115,115,46,99,111,109]
	var encryptedemail_id78='' //variable to contain encrypted email 
	for (var i=0; i<emailriddlerarray.length; i++)
	encryptedemail_id78+=String.fromCharCode(emailriddlerarray[i])

	let mailContacto = document.getElementById('mailContacto');
	if (mailContacto !== undefined && mailContacto !== null) {
		mailContacto.innerHTML = '<a href="mailto:'+encryptedemail_id78+'">'+ encryptedemail_id78 +'<span class="sr-only"> Clicka para enviar correo electrónico</span></a>';
	}
}

/*Convierte las imagenes de perfil en redondas*/
function adjustImgPerfil() {
	let imgPerfil = $("#profileImage")[0];
	if (typeof(imgPerfil) !== "undefined" && imgPerfil !== null) {
		imgPerfil.style.height = imgPerfil.clientWidth + "px";
	}
}

window.onload = function(){
	adjustUserDropdown();
	adjustImageContentSection();
	adjustVid();
	adjustFooterBottom();
	adjustMainHeight();
	adjustImgPerfil();
	encryptMail();
}

window.onresize = function(){
	adjustUserDropdown();
	adjustVid();
	adjustFooterBottom();
	adjustMainHeight();
	adjustImgPerfil();
}