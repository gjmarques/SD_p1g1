package serverSide;

import comInf.Message;
import comInf.MessageException;

public class BROMain extends Thread{
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
   * Baggage Reclaim Office Interface
   *
   *  @serialFieldBaggage Reclaim Office Interface
   */

   private BaggageReclaimOfficeInterface broInt;
  /**
   *  Instantiation of the interface to the barber shop.
   *
   *    @param sconi communication channel
   *    @param broInt Reclaim Office Interface
   */
   public BROMain (ServerCom sconi, BaggageReclaimOfficeInterface broInt)
   {
      super ("Proxy_" + BROMain.getProxyId ());

      this.sconi = sconi;
      this.broInt = broInt;
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
         outMessage = broInt.processAndReply (inMessage);             // processá-lo
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
      Class<?> cl = null;                                   // representação do tipo de dados BROMain na máquina
                                                            //   virtual de Java
      int proxyId;                                          // identificador da instanciação

      try
      { cl = Class.forName ("serverSide.BROMain");
      }
      catch (ClassNotFoundException e)
      { System.out.println("O tipo de dados BROMain não foi encontrado!");
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