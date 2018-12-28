package com.cw.wizbank.upload;

import com.cw.wizbank.util.cwException;
// state machine for parsing FB questions from EXCEL template
// 2000.08.13 wai

public class FBStateMachine extends Object {
    public final int node_que   = 1;
    public final int node_inter = 2;
    public final int node_opt   = 3;
    
    private final int max_nodeCol_que   = 5;
    private final int max_nodeCol_inter = 1;
    private final int max_nodeCol_opt   = 6;
    
    private int node;
    private int nodeIdx;
    private int nodeCol;
    private int interIdx;
    
    FBStateMachine() {
        node     = node_que;
        nodeIdx  = 0;
        nodeCol  = 0;
        interIdx = -1;
    }
    
    // methods for changing states
    private void que2inter() {
        node     = node_inter;
        nodeIdx  = 0;
        nodeCol  = 0;
        interIdx = 0;
    }
    private void inter2opt() {
        node     = node_opt;
        nodeIdx  = 0;
        nodeCol  = 0;
    }
    private void opt2opt() {
        nodeIdx++;
        nodeCol  = 0;
    }
    private void opt2inter() {
        node     = node_inter;
        nodeIdx  = 0;
        nodeCol  = 0;
        interIdx = interIdx + 1;
    }
    private void opt2que() {
        node     = node_que;
        nodeIdx  = 0;
        nodeCol  = 0;
        interIdx = -1;
    }
    private void remain() {
        nodeCol  = nodeCol + 1;
    }
    
    // check the current state and detemine the next state
    // lastEOL:  indicates whether there was a line break
    // nextTerm: the next term following
    // return:   whether the state has returned to the the initial position
    public boolean checkState(boolean lastEOL, String nextTerm) throws cwException {
        boolean stateReturn = false;
        
        switch (node) {
            // current state is _question_
            case node_que:
                if (lastEOL)
                    if (nodeCol < (max_nodeCol_que-1))
                        throw new cwException("attributes of question expected");
                    else
                        que2inter();
                else
                    remain();
                break;
            // current state is _interaction_
            case node_inter:
                if (lastEOL)
                    throw new cwException("attributes of options expected");
                else
                    inter2opt();
                break;
            // current state is _option_
            case node_opt:
                if (lastEOL)
                    if (nextTerm == null) {
                        // end of question
                        stateReturn = true;
                    } else if (nextTerm.length() > 0) {
                        // end of question with another question following
                        stateReturn = true;
                        opt2que();
                    } else {
                        // end of interaction with another interaction following
                        opt2inter();
                    }
                else
                    if (nodeCol == (max_nodeCol_opt-1) && nextTerm.length() > 0)
                        opt2opt();
                    else
                        remain();
                break;
        }
        
        return stateReturn;
    }
    
    // methods for accessing state variables
    public int currentNode() {
        return node;
    }
    public int currentIdx() {
        return nodeIdx;
    }
    public int currentCol() {
        return nodeCol;
    }
    public int currentInter() {
        return interIdx;
    }
}