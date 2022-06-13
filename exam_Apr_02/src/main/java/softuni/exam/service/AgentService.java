package softuni.exam.service;


import org.springframework.stereotype.Service;

import java.io.IOException;

public interface AgentService {

    boolean areImported();

    String readAgentsFromFile() throws IOException;
	
	String importAgents() throws IOException;
}
