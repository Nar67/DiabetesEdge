package com.diabetesedge.sid.agents.edge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class EmergencyAlarm extends Agent
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EmergencyAlarm.class);

    @Override
    protected void setup()
    {
        final DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());

        final ServiceDescription sdesc = new ServiceDescription();
        sdesc.setName("EmergencyAlarm");
        sdesc.setType("EmergencyAlarm");
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
                ACLMessage msg1 = blockingReceive();
                LOGGER.info("Recieved critical low levels of blood glucose. Calling for help.");
            }
        };

        this.addBehaviour(reciever);

    }
}