package serverSide;

import comInf.Message;
import comInf.MessageException;


/**
 * This type of data defines the service provider agent thread for a solution to the Airport Rapsody' Problem
 * that implements the type 2 client-server model (server replication) with static launch of shared regions threads.
 * Communication is based on passing messages over sockets using the TCP protocol.
 */
public class TSAProxy extends Thread {
    /**
     * Contador de threads lançados
     *
     * @serialField nProxy
     */
    private static int nProxy = 0;
    /**
     * Canal de comunicação
     *
     * @serialField sconi
     */
    private ServerCom sconi;

    /**
     * Temporary storage area interface
     */
    private TempStorageAreaInterface tsaInt;

    /**
       *  Temporary storage area interface
       *
       *    @param sconi canal de comunicação
       *    @param tsaInt Temporary storage area interface
       */
    
       public TSAProxy (ServerCom sconi, TempStorageAreaInterface tsaInt)
       {
          super ("Proxy_" + TSAProxy.getProxyId ());
    
          this.sconi = sconi;
          this.tsaInt = tsaInt;
       }

    /**
     * Ciclo de vida do thread agente prestador de serviço.
     */
    @Override
    public void run() {
        Message inMessage = null, // mensagem de entrada
                outMessage = null; // mensagem de saída

        inMessage = (Message) sconi.readObject(); // ler pedido do cliente
        try {
            outMessage = tsaInt.processAndReply(inMessage); // processá-lo
        } catch (MessageException e) {
            System.out.println("Thread " + getName() + ": " + e.getMessage() + "!");
            System.out.println(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage); // enviar resposta ao cliente
        sconi.close(); // fechar canal de comunicação
    }

    /**
     * Geração do identificador da instanciação.
     *
     * @return identificador da instanciação
     */
    private static int getProxyId() {
        Class<?> cl = null; // representação do tipo de dados ALProxy na máquina
                            // virtual de Java
        int proxyId; // identificador da instanciação

        try {
            cl = Class.forName("serverSide.TSAProxy");
        } catch (ClassNotFoundException e) {
            System.out.println("O tipo de dados TSAProxy não foi encontrado!");
            e.printStackTrace();
            System.exit(1);
        }

        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }

        return proxyId;
    }

    /**
     * Obtenção do canal de comunicação.
     *
     * @return canal de comunicação
     */

    public ServerCom getScon() {
        return sconi;
    }
}