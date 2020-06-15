package com.diabetesedge.sid.agents.user;

import static com.diabetesedge.sid.utils.MessageUtils.sendMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class HypoglycemiaAlarm extends Agent
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HypoglycemiaAlarm.class);

    @Override
    protected void setup()
    {
        final DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());

        final ServiceDescription sdesc = new ServiceDescription();
        sdesc.setName("HypoglycemiaAlarm");
        sdesc.setType("HypoglycemiaAlarm");
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
                LOGGER.info("Received low levels of glucose, sending alarm to App agent");
                sendMessage("App", msg1.getContent(), HypoglycemiaAlarm.this);
            }
        };

        this.addBehaviour(reciever);
    }
}
