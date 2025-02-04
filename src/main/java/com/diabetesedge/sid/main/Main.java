/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.diabetesedge.sid.main;

import java.io.FileNotFoundException;

import com.diabetesedge.sid.agents.Environment;
import com.diabetesedge.sid.agents.cloud.GlucosePredictor;
import com.diabetesedge.sid.agents.edge.CarbohydrateMeasurer;
import com.diabetesedge.sid.agents.edge.DosageRecommender;
import com.diabetesedge.sid.agents.edge.EmergencyAlarm;
import com.diabetesedge.sid.agents.edge.GlucoseSensor;
import com.diabetesedge.sid.agents.user.App;
import com.diabetesedge.sid.agents.user.HypoglycemiaAlarm;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;



public class Main {


    private static AgentContainer container;

    public static void main(final String[] args) throws FileNotFoundException, StaleProxyException
    {

        final Profile profile = new ProfileImpl();
        profile.setParameter(Profile.CONTAINER_NAME, "DIABETES_EDGE");
        profile.setParameter(Profile.GUI, "true");

        final Runtime runtime = Runtime.instance();
        runtime.createMainContainer(new ProfileImpl());
        container = runtime.createAgentContainer(profile);

        AgentController environment =
            container.createNewAgent("Environment", Environment.class.getName(), null);

        AgentController glucoseSensor =
            container.createNewAgent("GlucoseSensor", GlucoseSensor.class.getName(), null);

        AgentController CHMeasurer = container.createNewAgent("CarbohydrateMeasurer",
            CarbohydrateMeasurer.class.getName(), null);

        AgentController dosageRecommender =
            container.createNewAgent("DosageRecommender", DosageRecommender.class.getName(), null);

        AgentController glucosePredictor =
            container.createNewAgent("GlucosePredictor", GlucosePredictor.class.getName(), null);

        AgentController appAgent = container.createNewAgent("App", App.class.getName(), null);

        AgentController emergencyAlarm =
            container.createNewAgent("EmergencyAlarm", EmergencyAlarm.class.getName(), null);

        AgentController hypoAlarm =
            container.createNewAgent("HypoglycemiaAlarm", HypoglycemiaAlarm.class.getName(), null);

        environment.start();
        glucoseSensor.start();
        CHMeasurer.start();
        dosageRecommender.start();
        glucosePredictor.start();
        appAgent.start();
        emergencyAlarm.start();
        hypoAlarm.start();
    }

}
