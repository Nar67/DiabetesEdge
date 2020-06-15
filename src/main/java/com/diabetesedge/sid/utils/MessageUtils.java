package com.diabetesedge.sid.utils;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class MessageUtils
{

    public static void sendMessage(final String reciever, final String content,
        final Agent agent)
    {
        final DFAgentDescription desc = new DFAgentDescription();
        final ServiceDescription sdesc = new ServiceDescription();
        sdesc.setType(reciever);
        desc.addServices(sdesc);

        try
        {
            DFAgentDescription[] agents =
                DFService.search(agent, agent.getDefaultDF(), desc, new SearchConstraints());
            AID env = agents[0].getName();
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.setSender(agent.getAID());
            request.addReceiver(env);
            request.setContent(content);
            agent.send(request);
        }
        catch (FIPAException e)
        {
            e.printStackTrace();
        }
    }

    public final static ACLMessage sendRequestAndWaitResponse(final String reciever,
        final String content, final Agent agent)
    {
        final DFAgentDescription desc = new DFAgentDescription();
        final ServiceDescription sdesc = new ServiceDescription();
        sdesc.setType(reciever);
        desc.addServices(sdesc);

        try
        {
            DFAgentDescription[] agents =
                DFService.search(agent, agent.getDefaultDF(), desc, new SearchConstraints());
            AID env = agents[0].getName();
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.setSender(agent.getAID());
            request.addReceiver(env);
            request.setContent(content);
            agent.send(request);

            return agent.blockingReceive();

        }
        catch (FIPAException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static ACLMessage createReply(final ACLMessage request, final String content)
    {
        ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        reply.setContent(content);
        return reply;
    }

    public static String getSenderName(final ACLMessage msg)
    {
        return msg.getSender().getName().split("@")[0];
    }

}
