/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientlogic.exception;

/**
 *
 * @author adria
 */
public class LogicException extends Exception {

    /**
     * Creates a new instance of <code>LogicException</code> without detail
     * message.
     */
    public LogicException() {
    }

    /**
     * Constructs an instance of <code>LogicException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public LogicException(String msg) {
        super(msg);
    }
}
