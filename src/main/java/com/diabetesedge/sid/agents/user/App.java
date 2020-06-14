package com.diabetesedge.sid.agents.user;

import java.util.ArrayList;

import org.apache.jena.ext.com.google.common.base.CharMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class App extends Agent
{
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private final ArrayList<Integer> glucoseHistorical = new ArrayList<Integer>();

    private final ArrayList<Integer> carbHistorical = new ArrayList<Integer>();

    private final ArrayList<Integer> doseHistorical = new ArrayList<Integer>();

    private final ArrayList<Integer> predictionHistorial = new ArrayList<Integer>();

    @Override
    protected void setup()
    {
        final DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());

        final ServiceDescription sdesc = new ServiceDescription();
        sdesc.setName("App");
        sdesc.setType("App");
        agentDescription.addServices(sdesc);

        try
        {
            DFService.register(this, getDefaultDF(), agentDescription);
        }
        catch (FIPAException e)
        {
            e.printStackTrace();
        }

        CyclicBehaviour reciever = new CyclicBehaviour()
        {

            @Override
            public void action()
            {
                ACLMessage msg = blockingReceive();
                if (msg.getSender().getName().contains("GlucosePredictor"))
                {
                    System.out.println("App: " + msg.getContent());
                    if (CharMatcher.is('#').countIn(msg.getContent()) == 3)
                    {
                        String[] data = msg.getContent().split("#");
                        glucoseHistorical.add(Integer.getInteger(data[0]));
                        carbHistorical.add(Integer.getInteger(data[1]));
                        doseHistorical.add(Integer.getInteger(data[2]));
                        predictionHistorial.add(Integer.getInteger(data[3]));
                    }
                }
                else
                {
                    LOGGER.info("App recieved message indicating low level of blood glucose");
                }
            }
        };

        this.addBehaviour(reciever);

    }

}
