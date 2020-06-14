package com.diabetesedge.sid.agents.cloud;

import static com.diabetesedge.sid.utils.MessageUtils.sendMessage;
import static com.diabetesedge.sid.utils.MessageUtils.sendRequestAndWaitResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class GlucosePredictor extends Agent
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GlucosePredictor.class);

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

        CyclicBehaviour reciever = new CyclicBehaviour()
        {

            @Override
            public void action()
            {
                ACLMessage msg1 = blockingReceive();
                ACLMessage response =
                    sendRequestAndWaitResponse("Environment", "", GlucosePredictor.this);
                sendMessage("App", msg1.getContent() + "#" + response.getContent(),
                    GlucosePredictor.this);
            }
        };

        this.addBehaviour(reciever);

    }
}
