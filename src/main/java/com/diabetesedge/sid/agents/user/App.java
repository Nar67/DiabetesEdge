package com.diabetesedge.sid.agents.user;

import java.util.ArrayList;

import org.apache.jena.ext.com.google.common.base.CharMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.diabetesedge.sid.utils.DataPlotter;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class App extends Agent
{
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private final ArrayList<Integer> glucoseHistorical = new ArrayList<Integer>();

    private final ArrayList<Integer> carbHistorical = new ArrayList<Integer>();

    private final ArrayList<Integer> doseHistorical = new ArrayList<Integer>();

    private final ArrayList<Integer> predictionHistorial = new ArrayList<Integer>();

    @Override
    protected void setup()
    {
        final DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());

        final ServiceDescription sdesc = new ServiceDescription();
        sdesc.setName("App");
        sdesc.setType("App");
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
                ACLMessage msg = blockingReceive();
                if (msg.getSender().getName().contains("GlucosePredictor"))
                {
                    if (CharMatcher.is('#').countIn(msg.getContent()) == 3)
                    {
                        String[] data = msg.getContent().split("#");
                        glucoseHistorical.add(Integer.parseInt(data[0]));
                        carbHistorical.add(Integer.parseInt(data[1]));
                        doseHistorical.add(Integer.parseInt(data[2]));
                        predictionHistorial.add(Integer.parseInt(data[3]));
                        LOGGER
                            .info("Received data from multiple agents and stored to plot the data");
                    }
                }
                else
                {
                    LOGGER.info("App recieved message indicating low level of blood glucose");
                }
            }

        };

        this.addBehaviour(reciever);

        WakerBehaviour waker = new WakerBehaviour(this, 10000)
        {
            @Override
            protected void onWake()
            {
                LOGGER.info("Plotting data");
                final DataPlotter plotter =
                    new DataPlotter("Blood Glucose Data", glucoseHistorical);
                plotter.pack();
                plotter.setVisible(true);
            }
        };

        this.addBehaviour(waker);

    }

}
