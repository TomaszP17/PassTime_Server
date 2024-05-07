/**
 *
 *  @author Pluciński Tomasz S27477
 *
 */

package zad1;


import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * Umozliwia uruchamianie klietow w odrebnych watkach poprzez Executor Service
 */
public class ClientTask extends FutureTask<String> {
    public ClientTask(Client c, List<String> reqs, boolean showSendRes) {
        super(() -> {
            c.connect();
            c.send("login " + c.getId());
            for(String req : reqs) {
                String res = c.send(req);
                if (showSendRes) System.out.println(res);
            }
            return c.send("bye and log transfer");
        });
    }

    /**
     * Metoda tworzaca obiekty klasy ClientTask
     * @param c - klient
     * @param reqs - lista zapytan o uplyw czasu
     * @param showSendRes - czy pokazywac odpowiedzi serwera
     * @return - obiekt klasy ClientTask
     */
    public static ClientTask create(Client c, List<String> reqs, boolean showSendRes) {
        return new ClientTask(c, reqs, showSendRes);
    }
}
