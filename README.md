<div align="center">

<!-- Badges -->
![Latest Version](https://img.shields.io/maven-metadata/v?color=%20blue&label=latest%20version&metadataUrl=https%3A%2F%2Frepo.mrivanplays.com%2Frepository%2Fother-developers%2Fme%2Flokka30%2Ftreasury-api%2Fmaven-metadata.xml)
[![Support Discord](https://img.shields.io/discord/752310043214479462.svg?colorB=Blue&logo=discord&label=support)](https://discord.gg/HqZwdcJ) 
![Issues Open](https://img.shields.io/github/issues/lokka30/Treasury.svg)
![License](https://img.shields.io/github/license/lokka30/Treasury.svg)
[![MrIvanPlays CI Status](https://ci.mrivanplays.com/job/Treasury/badge/icon)](https://ci.mrivanplays.com/job/Treasury)

![Treasury Logo](https://i.ibb.co/gPN6x5P/Treasury250.png)

# Welcome to Treasury

A modern, multi-platform library taking plugin integrations to the next level.

Maintained by [lokka30](https://github.com/lokka30), [MrIvanPlays](https://github.com/MrIvanPlays) and [Jikoo](https://github.com/Jikoo), with [several kind contributors](https://github.com/lokka30/Treasury/wiki/Credits).

### [Download @ SpigotMC.org](https://www.spigotmc.org/resources/99531/) &bullet; [Read the Wiki](https://github.com/lokka30/Treasury/wiki)

#### [Frequently Asked Questions](https://github.com/lokka30/Treasury/wiki/Frequently-Asked-Questions) &bullet; [Integration Guide](https://github.com/lokka30/Treasury/wiki/Using-Treasury-in-your-Project) &bullet; [Support Discord](https://www.discord.io/arcaneplugins)

</div>

***

## About

Treasury is a library which is designed to provide multiple APIs for the plugins on your Minecraft server to forge strong integrations with each other. Treasury currently provides its staple Economy API, among a few others such as an Events and Services API which make it easier for plugins to work on a wider range of platforms.

<details>
<summary>I'm confused... 'APIs'?</summary>

This can be thought of as Treasury providing a different languages to speak for all of the plugins on your server, each language being suited for a different purpose. Treasury offers an Economy language for your Economy plugins to speak, the developers of the Economy plugins just need to tweak their plugins to speak the language so that it can automatically integrate with every other plugin that does so. For instance, a Job and Shop plugin which use the Economy API are able to indirectly communicate with an economy provider that supports the API and form a functional virtual economy on your server.

</details>
<br />

Treasury currently provides the following APIs: Economy, Services, Events. [Several more are planned](https://github.com/lokka30/Treasury/issues/131) for the future, such as Permissions and Chat.

The scope of this library is narrowly focused on making it easy to facilitate powerful integrations between plugins on multiple platforms.

This is scratching the surface – we encourage you to read more about the library:

#### [Learn more about Treasury here: our mission, why you should choose it, and more!](https://github.com/lokka30/Treasury/wiki/About-Treasury)

***

## License

Treasury is free, libre software.

<details>
<summary>MIT License</summary>

> Copyright (c) 2021-2022 Treasury contributors
>
> Permission is hereby granted, free of charge, to any person
> obtaining a copy of this software and associated documentation 
> files (the “Software”), to deal in the Software without restriction,
> including without limitation the rights to use, copy, modify,
> merge, publish, distribute, sublicense, and/or sell copies of the
> Software, and to permit persons to whom the Software is furnished
> to do so, subject to the following conditions:
>
> The above copyright notice and this permission notice shall be
> included in all copies or substantial portions of the Software.
>
> THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
> EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
> OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
> NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
> HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
> WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
> FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
> OTHER DEALINGS IN THE SOFTWARE.

</details>

## bStats Metrics

Never heard of bStats? Read more below.

<details>
<summary>View Notice</summary>

> Treasury utilizes bStats metrics, as do thousands of other Minecraft plugins and software, from PaperMC to EssentialsX. This service collects very basic data on the server which is all public, e.g., how many servers are running Treasury, how many players are online, and so on. None of this data can be pinpointed back to a individual server, they all contribute to a single pool of data.
>
> All of the collected data is available [here](https://bstats.org/plugin/bukkit/Treasury/12927).
> 
> To change whether bStats metrics should run on your server, simply visit the `/plugins/bStats/config.yml` file and edit it to your preference.

</details>