/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package it.eng.spagoLite.form.wizard;

import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.base.BaseElements;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Wizard extends BaseElements<WizardElement> {

    private static final long serialVersionUID = 1L;
    private static final Logger Logger = LoggerFactory.getLogger(Wizard.class);

    public enum WizardNavigation {
        Prev, Next, Save, Cancel
    }

    private WizardElement currentElement;
    private boolean hideStepCounter;

    public Wizard(Component parent, String name, String description, boolean hideStepCounter) {
        super(parent, name, description);
    }

    public WizardElement getCurrentElement() {
        return currentElement;
    }

    public boolean isHideStepCounter() {
        return hideStepCounter;
    }

    public void setHideStepCounter(boolean hideStepCounter) {
        this.hideStepCounter = hideStepCounter;
    }

    public boolean hasEndPage() {
        for (WizardElement wizardElement : getComponentList()) {
            if (wizardElement instanceof EndPage) {
                return true;
            }
        }

        return false;
    }

    public List<Step> getStepList() {
        List<Step> result = new ArrayList<Step>();
        for (WizardElement wizardElement : getComponentList()) {
            if (wizardElement instanceof Step) {
                result.add((Step) wizardElement);
            }
        }

        return result;
    }

    public Step getCurrentStep() {
        List<Step> list = getStepList();

        if (list.size() == 0) {
            return null;
        }

        for (Step step : list) {
            if (step.isCurrent())
                return step;
        }

        return list.get(0);
    }

    private int getCurrentStepIndex() {
        int i = 0;
        for (Step step : getStepList()) {
            if (step.isCurrent()) {
                return i;
            } else {
                i++;
            }
        }

        return 0;
    }

    private void setCurrentElement(WizardElement currentElement) {
        for (Step step : getStepList()) {
            if (currentElement == step) {
                step.setCurrent(true);
            } else {
                step.setCurrent(false);
            }
        }
        this.currentElement = currentElement;
    }

    public Step getFirstStep() {
        List<Step> list = getStepList();
        if (list != null && list.size() > 0) {
            for (Step step : list) {
                if (!step.isHidden()) {
                    return step;
                }
            }
        }

        Logger.error("Attenzione, un wizard deve avere almeno uno step");
        return null;
    }

    public Step getLastStep() {
        List<Step> list = getStepList();
        if (list != null && list.size() > 0) {
            for (int i = list.size() - 1; i >= 0; i--) {
                if (!list.get(i).isHidden()) {
                    return list.get(i);
                }
            }
        }

        Logger.error("Attenzione, un wizard deve avere almeno uno step");
        return null;
    }

    public EndPage getEndPage() {
        EndPage result = null;
        for (WizardElement element : getComponentList()) {
            if (element instanceof EndPage) {
                result = (EndPage) element;
            }
        }
        return result;
    }

    public boolean isFirstStep() {
        return getCurrentElement() == getFirstStep();
    }

    public boolean isLastStep() {
        return getCurrentElement() == getLastStep();
    }

    public boolean isEndPage() {
        return getCurrentElement() == getEndPage();
    }

    public void reset() {
        setCurrentElement(getFirstStep());
    }

    /**
     * Conta solo gli step visibili
     * 
     * @return conteggio
     */
    public int stepSize() {
        int i = 0;
        List<Step> list = getStepList();
        if (list != null && list.size() > 0) {
            for (Step step : list) {
                if (!step.isHidden()) {
                    i++;
                }
            }
        }
        return i;
    }

    /**
     * Conta solo gli step visibili
     * 
     * @return conteggio
     */
    public int currentStepIndex() {
        int i = 0;
        for (Step step : getStepList()) {
            if (step.isCurrent()) {
                return i;
            } else if (!step.isHidden()) {
                i++;
            }
        }

        return 0;
    }

    public void goToEndPage() {
        EndPage endPage = getEndPage();

        if (endPage != null) {
            setCurrentElement(endPage);
        } else {
            Logger.error("Attenzione, il wizard non ha l'end page");
        }
    }

    /**
     * Va al primo step visibile
     */
    public void goToFirstStep() {
        setCurrentElement(getFirstStep());
    }

    /**
     * Va all'ultimo step visibile
     */
    public void goToLastStep() {
        setCurrentElement(getLastStep());
    }

    /**
     * Va allo step successivo visibile
     */
    public void goToNextStep() {
        List<Step> list = getStepList();
        if (list != null && list.size() > 0) {
            for (int i = getCurrentStepIndex() + 1; i < list.size(); i++) {
                if (!list.get(i).isHidden()) {
                    getCurrentStep().setCurrent(false);
                    list.get(i).setCurrent(true);
                    setCurrentElement(list.get(i));
                    return;
                }
            }
        }
    }

    /**
     * Va allo step precedente visibile
     */
    public void goToPrevStep() {
        List<Step> list = getStepList();
        if (list != null && list.size() > 0) {
            for (int i = getCurrentStepIndex() - 1; i >= 0; i--) {
                if (!list.get(i).isHidden()) {
                    getCurrentStep().setCurrent(false);
                    list.get(i).setCurrent(true);
                    setCurrentElement(list.get(i));
                    return;
                }
            }
        }
    }

}
