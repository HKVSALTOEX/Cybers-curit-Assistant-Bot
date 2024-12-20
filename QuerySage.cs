using System;
using System.Threading.Tasks;
using Discord;
using Discord.Commands;
using Discord.WebSocket;

public class QuerySage
{
    private DiscordSocketClient _client;
    private CommandService _commands;
    private IServiceProvider _services;

    public async Task MainAsync()
    {
        _client = new DiscordSocketClient();
        _commands = new CommandService();

        _client.Log += LogAsync;
        _client.MessageReceived += HandleCommandAsync;

        var token = "YOUR_BOT_TOKEN";
        await _client.LoginAsync(TokenType.Bot, token);
        await _client.StartAsync();

        await Task.Delay(-1);
    }

    private Task LogAsync(LogMessage log)
    {
        Console.WriteLine(log.ToString());
        return Task.CompletedTask;
    }

    private async Task HandleCommandAsync(SocketMessage messageParam)
    {
        var message = messageParam as SocketUserMessage;
        var context = new SocketCommandContext(_client, message);

        if (message == null || message.Author.IsBot) return;

        int argPos = 0;
        if (message.HasCharPrefix('!', ref argPos))
        {
            var result = await _commands.ExecuteAsync(context, argPos, _services);
            if (!result.IsSuccess) Console.WriteLine(result.ErrorReason);
        }
    }
}

public class QuestionModule : ModuleBase<SocketCommandContext>
{
    [Command("question")]
    public async Task QuestionAsync([Remainder] string question)
    {
        var channel = Context.Guild.TextChannels.FirstOrDefault(c => c.Name == "questions-réponses");
        if (channel != null)
        {
            await channel.SendMessageAsync($"{Context.User.Mention} a posé une question : {question}");
        }
    }
}