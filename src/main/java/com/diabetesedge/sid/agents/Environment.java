package com.diabetesedge.sid.agents;

import static com.diabetesedge.sid.utils.MessageUtils.createReply;

import java.util.Random;

import org.apache.log4j.Logger;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Environment extends Agent
{
    private static final Logger LOGGER = Logger.getLogger(Environment.class);

    @Override
    protected void setup()
    {
        final DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());

        final ServiceDescription sdesc = new ServiceDescription();
        sdesc.setName("Environment");
        sdesc.setType("Environment");
        agentDescription.addServices(sdesc);

        try
        {
            DFService.register(this, getDefaultDF(), agentDescription);
        }
        catch (FIPAException e)
        {
            e.printStackTrace();
        }

        final ParallelBehaviour parallel = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ALL);

        final CyclicBehaviour responder = new CyclicBehaviour(this)
        {
            @Override
            public void action()
            {
                ACLMessage msg = blockingReceive();
                manageRequest(msg);

            }

        };

        parallel.addSubBehaviour(responder);
        this.addBehaviour(parallel);

    }

    private void manageRequest(final ACLMessage msg)
    {
        if (msg.getSender().getName().contains("GlucoseSensor"))
        {
            send(createReply(msg, Integer.toString(new Random().nextInt(330) + 30)));

        }
        else if (msg.getSender().getName().contains("CarbohydrateMeasurer"))
        {
            send(createReply(msg, Integer.toString(new Random().nextInt(120))));
        }
    }
}
