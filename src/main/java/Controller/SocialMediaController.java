package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    
    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::registerUserHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::createMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        app.patch("messages/{message_id}", this::updateMessageHandler);
        app.get("accounts/{account_id}/messages", this::getAllMessagesByUserIdHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account newAccount = om.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.registerAccount(newAccount);
        if (addedAccount == null) {
            ctx.status(400);
        }
        else {
            ctx.json(om.writeValueAsString(addedAccount));
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account loginInput = om.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.canLogin(loginInput);
        if (loggedInAccount == null) {
            ctx.status(401);
        }
        else {
            ctx.json(om.writeValueAsString(loggedInAccount));
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Message newMessage = om.readValue(ctx.body(), Message.class);
        Message createResult = messageService.createMessage(newMessage);
        if (createResult == null) {
            ctx.status(400);
        }
        else {
            ctx.json(om.writeValueAsString(createResult));
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ctx.json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        Message message = messageService.getMessageById(Integer.parseInt(ctx.pathParam("message_id")));
        if (message == null) {
            ctx.json("");
        }
        else {
            ctx.json(message);
        }
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        Message message = messageService.deleteMessageById(Integer.parseInt(ctx.pathParam("message_id")));
        if (message == null) {
            ctx.json("");
        }
        else {
            ctx.json(message);
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Message newMessage = om.readValue(ctx.body(), Message.class);
        Message createResult = messageService.updateMessageById(Integer.parseInt(ctx.pathParam("message_id")), newMessage);
        if (createResult == null) {
            ctx.status(400);
        }
        else {
            ctx.json(createResult);
        }
    }

    private void getAllMessagesByUserIdHandler(Context ctx) throws JsonProcessingException {
        ctx.json(messageService.getAllMessagesByUserId(Integer.parseInt(ctx.pathParam("account_id"))));
    }
}