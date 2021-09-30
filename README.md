# 💼 Welcome to Treasury

## ⚠ This resource is work-in-progress. :)

## 📜 What is Treasury?

Treasury is a possibly upcoming resource containing a modern API to facilitate virtual money
transactions on Minecraft servers between Economy providers (aka. Economy plugins) and plugins
that wish to use them (e.g. JobsReborn, ShopGUIPlus).

**It is currently unlikely this will ever be released unless it gains enough traction.**

Like Vault, Treasury is installed as a plugin on a server. The plugin only offers an information
command and update checker. Treasury is used by economy provider plugins through Bukkit's
Service Manager.

## ❓ Why Treasury?

The following is my personal opinion ([lokka30](https://github.com/lokka30)).

I have a drive to make things better for server owners and plugin developers. I've found that the
existing economy APIs are unsuitable for modern economy plugins, at least to my own criteria.

Although I can deal with using [Vault](https://github.com/MilkBowl/Vault), it has not advanced its aging code which restricts the
capabilities of Economy providers through its limitations. For example, its API does not directly
support non-player transactions (you have to use their deprecated methods which breaks economy
provider plugins) and has zero multi currency support.

I think [Reserve](https://github.com/TheNewEconomy/Reserve) tried to get at this, but I think it is too bloated, and I really prefer the
simplistic roots of Vault. Whilst Reserve is harder to use, Treasury is almost as simple as Vault
to use in plugins. Reserve has been out for a few years and has seen very limited success especially
relative to Vault. This is likely because plugin developers do not see it fit for adoption.

Vault and Reserve are the only key APIs currently out there that offer anything substantial and have
sufficient usage.

## 📂 Where can I download Treasury?

Treasury is not released so there are no compiled files available. Please compile your own or
contact lokka30 if you do not want to or can't compile it.

Once released, it will be available on [SpigotMC.org](https://www.spigotmc.org).

## 📃 What's left to-do?

In descending order:

- Get lots of opinions on the design of the API so it can be improved before release
- Add config file loading code
- Add functionality to the commands
- Make all chat messages customisable
- Add update checker
- Add javadocs
- Add documentation
- Test it
- Release it

## ☢ How stable is it?

It's in alpha stage at the moment. I am aiming to make it as stable as possible after it is released.
This involves a lot of peer review to make sure the API is suitable, easy to use, and future-proof.

## 💬 I'd like to chat / request assistance / report an issue / suggest something. Where can I go?

In descending order of my preference - choose whichever suits you best:

* [Post on the Issue Tracker](https://github.com/lokka30/Treasury/issues)
* [Join my Discord Server](https://www.discord.io/arcaneplugins)
* [Post in the Discussions Section on this repository](https://github.com/lokka30/Treasury/discussions)
* [PM me on SpigotMC.org](https://www.spigotmc.org/conversations/add?to=lokka30)

## 🤔 Vault is used on most servers. How will Treasury even compete?

I have no idea. If it turns out to be a great API then it might be adopted by plugin developers,
which should drive more server owners to use it, which then drives more plugin developers to use it.
But it's unlikely anyone can beat Vault as it has been the only decent API (in my opinion - and this
is backed up by the amount of servers and plugins using it) for *ages*, rather Treasury will be
new against the big dog.

## 👩‍⚖️ What license does Treasury use?

Treasury will always be free and open source.

```
Treasury is Copyright (C) 2021  lokka30

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You have received a copy of the GNU Affero General Public License
with this program - please see the LICENSE.md file. Alternatively,
please visit the <https://www.gnu.org/licenses/> website.
```

## 🧱 What plugins will work with Treasury when it is released?

Here are a list of plugins known to have Treasury support planned upon release:
* [NanoEconomy](https://github.com/lokka30/NanoEconomy) by [lokka30](https://github.com/lokka30) - unreleased
* [ElementalEconomy](https://github.com/lokka30/ElementalEconomy) by [lokka30](https://github.com/lokka30) - unreleased
* Speaking to other developers about this resource's adoption as well.