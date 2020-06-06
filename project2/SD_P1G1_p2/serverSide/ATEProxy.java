package serverSide;

import comInf.Message;
import comInf.MessageException;

/**
 * This type of data defines the service provider agent thread for a solution to the Airport Rapsody' Problem
 * that implements the type 2 client-server model (server replication) with static launch of shared regions threads.
 * Communication is based on passing messages over sockets using the TCP protocol.
 */
public class ATEProxy extends Thread {

    /**
     *  threads counter
     *
     *  @serialField nProxy
     */
    private static int nProxy = 0;
    /**
     *  Communication channel
     *
     *  @serialField sconi
     */
    private ServerCom sconi;
    /**
     *  Arrival Lounge Interface
     *
     *  @serialField Arrival lounge interface
     */

    private ArrivalTerminalExitInterface ateInt;
    /**
     *  Instantiation of the interface 
     *
     *    @param sconi communication channel
     *    @param ateInt Arrival Terminal Exit Interface interface
     */
    public ATEProxy (ServerCom sconi, ArrivalTerminalExitInterface ateInt){
        super ("Proxy_" + ATEProxy.getProxyId ());

        this.sconi = sconi;
        this.ateInt = ateInt;
   }

  /**
   *  Ciclo de vida do thread agente prestador de serviço.
   */
   @Override
   public void run (){
      Message inMessage = null,                                      // mensagem de entrada
              outMessage = null;                                     // mensagem de saída

      inMessage = (Message) sconi.readObject ();                     // ler pedido do cliente
      try{ 
         outMessage = ateInt.ProcessAndReply (inMessage);             // processá-lo
      }
      catch (MessageException e)
      { System.out.println("Thread " + getName () + ": " + e.getMessage () + "!");
        System.out.println(e.getMessageVal ().toString ());
        System.exit (1);
      }
      sconi.writeObject (outMessage);                                // enviar resposta ao cliente
      sconi.close ();                                                // fechar canal de comunicação
   }

   /**
   *  Geração do identificador da instanciação.
   *
   *    @return identificador da instanciação
   */
   private static int getProxyId ()
   {
      Class<?> cl = null;                                   // representação do tipo de dados ATEProxy na máquina
                                                            //   virtual de Java
      int proxyId;                                          // identificador da instanciação

      try
      { cl = Class.forName ("serverSide.ATEProxy");
      }
      catch (ClassNotFoundException e)
      { System.out.println("O tipo de dados ATEProxy não foi encontrado!");
         e.printStackTrace ();
         System.exit (1);
      }

      synchronized (cl)
      { proxyId = nProxy;
         nProxy += 1;
      }

      return proxyId;
   }

   /**
    *  Obtenção do canal de comunicação.
   *
   *    @return canal de comunicação
   */

   public ServerCom getScon ()
   {
      return sconi;
   }

}