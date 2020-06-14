package com.diabetesedge.sid.agents.edge;

import static com.diabetesedge.sid.utils.MessageUtils.sendMessage;
import static com.diabetesedge.sid.utils.MessageUtils.sendRequestAndWaitResponse;

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

public class DosageRecommender extends Agent
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DosageRecommender.class);

    @Override
    protected void setup()
    {
        final DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());

        final ServiceDescription sdesc = new ServiceDescription();
        sdesc.setName("DosageRecommender");
        sdesc.setType("DosageRecommender");
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
                ACLMessage msg2 = blockingReceive();
                ACLMessage response =
                    sendRequestAndWaitResponse("Environment", "", DosageRecommender.this);
                String content = setPredictorContent(msg1, msg2, response);
                System.out.println("Recommender: " + content);
                sendMessage("GlucosePredictor", content, DosageRecommender.this);
            }
        };

        this.addBehaviour(reciever);

    }

    protected String setPredictorContent(final ACLMessage msg1, final ACLMessage msg2, final ACLMessage response)
    {
        String content = "";
        if(msg1.getSender().getName().contains("GlucoseSensor"))
        {
            content += msg1.getContent() + "#";
        }
        else if(msg2.getSender().getName().contains("GlucoseSensor"))
        {
            content += msg2.getContent() + "#";
        }

        if (!content.equals("") && msg1.getSender().getName().contains("CarbohydrateMeasurer"))
        {
            content += msg1.getContent() + "#";
        }
        else if (!content.equals("") && msg2.getSender().getName().contains("CarbohydrateMeasurer"))
        {
            content += msg2.getContent() + "#";
        }

        if (CharMatcher.is('#').countIn(content) == 2)
        {
            content += response.getContent();
            return content;
        }
        return "";
    }
}