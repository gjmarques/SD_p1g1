package serverSide;

import comInf.Message;
import comInf.MessageException;

public class BCPProxy extends Thread{
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
 
    private BaggageCollectionPointInterface bcpInt;
   /**
    *  Instantiation of the interface 
    *
    *    @param sconi communication channel
    *    @param bcpInt baggage collection point interface
    */
    public BCPProxy (ServerCom sconi, BaggageCollectionPointInterface bcpInt)
    {
       super ("Proxy_" + BCPProxy.getProxyId ());
 
       this.sconi = sconi;
       this.bcpInt = bcpInt;
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
          outMessage = bcpInt.ProcessAndReply (inMessage);             // processá-lo
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
       Class<?> cl = null;                                   // representação do tipo de dados BCPProxy na máquina
                                                             //   virtual de Java
       int proxyId;                                          // identificador da instanciação
 
       try
       { cl = Class.forName ("serverSide.BCPProxy");
       }
       catch (ClassNotFoundException e)
       { System.out.println("O tipo de dados BCPProxy não foi encontrado!");
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