package de.saxsys.campus.jsf;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.saxsys.campus.business.DbSlotManager;
import de.saxsys.campus.domain.Slot;

@Named
@ConversationScoped
public class AdminViewBean implements Serializable {

	private static final long serialVersionUID = 3238657117255311412L;
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminViewBean.class);

	private List<Slot> slots;

	@Inject
	private DbSlotManager slotManager;

	@PostConstruct
	public void init() {
		slots = slotManager.allSlots();
	}

	public void newSlot() {
		LOGGER.debug("Create new slot");
	}

	public void deleteSlot() {
		LOGGER.debug("Delete slot");
	}

	public void editSlot() {
		LOGGER.debug("Edit slot");
	}

	public List<Slot> getSlots() {
		return slots;
	}

	public void setSlots(List<Slot> slots) {
		this.slots = slots;
	}

}