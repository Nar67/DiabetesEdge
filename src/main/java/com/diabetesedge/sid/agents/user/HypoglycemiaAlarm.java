package com.diabetesedge.sid.agents.user;

import org.apache.log4j.Logger;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class HypoglycemiaAlarm extends Agent
{
    private static final Logger LOGGER = Logger.getLogger(HypoglycemiaAlarm.class);

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

    }
}
