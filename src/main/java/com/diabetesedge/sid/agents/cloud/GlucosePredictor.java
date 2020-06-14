package com.diabetesedge.sid.agents.cloud;

import org.apache.log4j.Logger;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class GlucosePredictor extends Agent
{
    private static final Logger LOGGER = Logger.getLogger(GlucosePredictor.class);

    @Override
    protected void setup()
    {
        final DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());

        final ServiceDescription sdesc = new ServiceDescription();
        sdesc.setName("GlucosePredictor");
        sdesc.setType("GlucosePredictor");
        agentDescription.addServices(sdesc);

        try
        {
            DFService.register(this, getDefaultDF(), agentDescription);
        }
        catch (FIPAException e)
        {
            e.printStackTrace();
        }

    }
}
