package com.diabetesedge.sid.agents.edge;

import static com.diabetesedge.sid.utils.MessageUtils.sendRequestAndWaitResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class GlucoseSensor extends Agent
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GlucoseSensor.class);

    @Override
    protected void setup()
    {
        final DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());

        final ServiceDescription sdesc = new ServiceDescription();
        sdesc.setName("GlucoseSensor");
        sdesc.setType("GlucoseSensor");
        agentDescription.addServices(sdesc);

        try
        {
            DFService.register(this, getDefaultDF(), agentDescription);
        }
        catch (FIPAException e)
        {
            e.printStackTrace();
        }

        TickerBehaviour ticker = new TickerBehaviour(this, 1000)
        {
            @Override
            protected void onTick()
            {
                ACLMessage msg = sendRequestAndWaitResponse("Environment", "", GlucoseSensor.this);
                LOGGER.info("Content: " + msg.getContent());
            }
        };

        this.addBehaviour(ticker);

    }



}