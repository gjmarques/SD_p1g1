package serverSide;

import comInf.Message;
import comInf.MessageException;

public class GIRProxy extends Thread{
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

    private GenInfoRepoInterface girInt;
    /**
     *  Instantiation of the interface  general information repository
     *
     *    @param sconi communication channel
     *    @param girInt general information repository interface
     */
    public GIRProxy (ServerCom sconi, GenInfoRepoInterface girInt)
    {
        super ("Proxy_" + GIRProxy.getProxyId ());

        this.sconi = sconi;
        this.girInt = girInt;
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
            outMessage = girInt.processAndReply (inMessage);             // processá-lo
        }
        catch (MessageException e){ 
            System.out.println("Thread " + getName () + ": " + e.getMessage () + "!");
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
        Class<?> cl = null;                                   // representação do tipo de dados GIRProxy na máquina
                                                            //   virtual de Java
        int proxyId;                                          // identificador da instanciação
        try{ 
            cl = Class.forName ("serverSide.GIRProxy");
        }
        catch (ClassNotFoundException e)
        { System.out.println("O tipo de dados GIRProxy não foi encontrado!");
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