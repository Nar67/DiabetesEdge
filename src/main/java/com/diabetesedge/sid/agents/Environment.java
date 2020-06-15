package com.diabetesedge.sid.agents;

import static com.diabetesedge.sid.utils.MessageUtils.createReply;
import static com.diabetesedge.sid.utils.MessageUtils.getSenderName;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(Environment.class);

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
        if (msg.getSender().getName().contains("GlucoseSensor")
            || msg.getSender().getName().contains("GlucosePredictor"))
        {
            LOGGER.info("Generated glucose level, sending to agent {}", getSenderName(msg));
            send(createReply(msg, Integer.toString(new Random().nextInt(330) + 30)));

        }
        else if (msg.getSender().getName().contains("CarbohydrateMeasurer"))
        {
            LOGGER.info("Generated carbohydrate measure, sending to agent {}", getSenderName(msg));
            send(createReply(msg, Integer.toString(new Random().nextInt(120))));
        }
        else if (msg.getSender().getName().contains("DosageRecommender"))
        {
            LOGGER.info("Generated Insulin dosage recommender, sending to agent {}",
                getSenderName(msg));
            send(createReply(msg, Integer.toString(new Random().nextInt(30))));
        }
    }
}
