package it.eng.spagoLite.form.wizard;

import it.eng.spagoLite.form.Component;

public interface WizardElement extends Component {

    public boolean isCurrent();

    public void setCurrent(boolean current);
}
