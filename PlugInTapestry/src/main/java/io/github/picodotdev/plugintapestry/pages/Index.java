package io.github.picodotdev.plugintapestry.pages;

import io.github.picodotdev.plugintapestry.misc.Globals;
import io.github.picodotdev.plugintapestry.services.annotation.Csrf;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @tapestrydoc
 */
public class Index {

	@Property
	@Persist(value = PersistenceConstants.SESSION)
	private Long cuenta;

	@Property
	@Persist(value = PersistenceConstants.FLASH)
	private List colores;

	@Component
	private Zone zone;

	@Component
	private Zone submitOneZone;

	@Component
	private Zone csrfZone;

	@Inject
	private AjaxResponseRenderer renderer;
	
	@Inject
	private Messages messages;

	// Ciclo de vida
	Object onActivate(String context) {
		if (context != null) {
			return Error404.class;
		}
		return null;
	}

	/**
	 * Método del ciclo de vida de la página que es llamado por Tapestry al inicio de la
	 * renderización de la página.
	 */
	void setupRender() {
		// ThreadLocal example
		System.out.printf("Host (from page): %s%n", Globals.HOST.get());

		if (cuenta == null) {
			// Iniciarlizar el valor de la cuenta al cargar la página
			cuenta = 0l;
		}
		if (colores == null) {
			colores = new ArrayList();			
		}
	}

	// Eventos
	/**
	 * Esta acción permite ver el informe de error generado por Tapestry, además de la traza de la
	 * excepción, el informe incluye mucha más información (extracto de la plantilla, información de
	 * la petición, del entorno, ...)
	 */
	void onActionFromInformeError() throws Exception {
		throw new Exception("Sí, ese enlace produce una excepción");
	}

	/**
	 * Evento que suma uno a la cuenta.
	 */
	void onActionFromSumar1Cuenta() {
		cuenta += 1;
	}

	/**
	 * Evento que también suma uno a la cuenta pero con un nombre de evento propio y sin estar
	 * asociado a un determinado componente.
	 */
	void onSumar1Cuenta() {
		cuenta += 1;
	}

	/**
	 * Evento que suma uno a la cuenta (via Ajax).
	 */
	void onActionFromSumar1CuentaAjax() throws Exception {
		// if (1 == 1) throw new Exception("Sí, ese enlace produce una excepción");

		cuenta += 1;
		// Actualizar una zona
		// return zone.getBody()
		// Actualizar varias zonas
		renderer.addRender("zone", zone).addRender("submitOneZone", submitOneZone).addRender("csrfZone", csrfZone);
	}
	
    void onPrepareForSubmitFromColoresForm() {
    	colores = new ArrayList();
    }

	void onSumar1CuentaSubmitOne() throws Exception {
		Thread.sleep(3000);
		cuenta += 1;
	}

	void onSumar1CuentaAjaxSubmitOne() throws Exception {
		Thread.sleep(3000);
		cuenta += 1;
		renderer.addRender("zone", zone).addRender("submitOneZone", submitOneZone).addRender("csrfZone", csrfZone);
	}

	void onSuccessFromSubmitOneForm1() throws Exception {
		onSubmitOne();
	}

	void onSuccessFromSubmitOneForm2() throws Exception {
		onSubmitOne();
	}

	void onSuccessFromSubmitOneForm3() throws Exception {
		onSubmitOne();
	}

	private void onSubmitOne() throws Exception {
		Thread.sleep(3000);
		cuenta += 1;
		renderer.addRender("zone", zone).addRender("submitOneZone", submitOneZone).addRender("csrfZone", csrfZone);
	}

	@Csrf
	void onSuccessFromCsrfForm() {
		cuenta += 1;
		renderer.addRender("zone", zone).addRender("submitOneZone", submitOneZone).addRender("csrfZone", csrfZone);
	}

	@Csrf
	void onSumar1CuentaCsrf() {
		cuenta += 1;
		renderer.addRender("zone", zone).addRender("submitOneZone", submitOneZone).addRender("csrfZone", csrfZone);
	}

	/**
	 * Evento que reinicializa la cuenta.
	 */
	@RequiresPermissions("cuenta:reset")
	void onActionFromReiniciarCuenta() {
		cuenta = 0l;
	}

	/**
	 * Evento que cierra la sesión del usuario actual.
	 */
	void onActionFromCerrarSesion() {
		SecurityUtils.getSecurityManager().logout(getSubject());
	}

	/**
	 * Devuelve el objeto que representa al usuario autenticado.
	 */
	public WebDelegatingSubject getSubject() {
		return (WebDelegatingSubject) SecurityUtils.getSubject();
	}
	
	public String getMensajeFormasPlurales(String key, long num) {
	    // Obtener el mensaje con las diferentes formas plurales
	    if (!messages.contains(key)) {
	        return messages.get(key);
	    }
	    
	    String message = messages.get(key);

	    // Seleccionar la forma plural adecuada del mensaje
	    ChoiceFormat format = new ChoiceFormat(message);
	    String pluralized = format.format(num);

	    // Realizar la interpolación de variables
	    return MessageFormat.format(pluralized, num);
	}
}