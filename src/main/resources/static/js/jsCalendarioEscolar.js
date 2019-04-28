$('#calendar').fullCalendar({
	locale: 'es',
    header: {
        left: 'prev,next today',
        center: 'title',
        right: 'month,listMonth'
    },
    navLinks: false,
    fixedWeekCount: false,
    //editable: true,
    eventLimit: true,
    eventColor: '#2e2e2e',
    eventBorderColor: '#ddd',
    eventTextColor: '#FFFFFF',

    events: [
    	{
            title: 'No Lectivo',
            start: '2018-10-12',
            tooltip: 'Dia de la Hispanidad'
        },
        {
            title: 'No Lectivo',
            start: '2018-11-01',
            end: '2018-11-03',
            tooltip: 'Festividad de Todos los Santos'
        },
        {
            title: 'No Lectivo',
            start: '2018-12-06',
            end: '2018-12-08',
            tooltip: 'Día de la Constitución Española'
        },
        {
            title: 'Vacaciones de Navidad',
            start: '2018-12-22',
            end: '2019-01-08',
            tooltip: 'Vacaciones de Navidad'
        },
        {
            title: 'No Lectivo',
            start: '2019-03-01',
            tooltip: 'No Lectivo'
        },
        {
            title: 'No Lectivo',
            start: '2019-03-04',
            tooltip: 'No Lectivo'
        },
        {
            title: 'Vacaciones de Semana Santa',
            start: '2019-04-12',
            end: '2019-04-23',
            tooltip: 'Vacaciones de Semana Santa'
        },
        {
            title: 'No Lectivo',
            start: '2019-05-01',
            tooltip: 'Día del trabajo'
        },
        {
            title: 'No Lectivo',
            start: '2019-05-02',
            tooltip: 'Día de la Comunidad de Madrid'
        },
        {
            title: 'No Lectivo',
            start: '2019-05-03',
            tooltip: 'No Lectivo'
        },
        {
            title: 'Final Evaluación Ordinaria',
            start: '2019-06-07',
            tooltip: 'Final Evaluación Ordinaria'
        },
        {
            title: 'Inicio Evaluación Extraordinaria',
            start: '2019-06-10',
            tooltip: 'Inicio Evaluación Extraordinaria'
        },
        {
            title: 'Final del curso escolar',
            start: '2019-06-21',
            tooltip: 'Final del curso escolar'
        }
    ],
    eventAfterRender: function (event, element) {
        $(element).tooltip({title:event.tooltip, container: "body"});
	}
});