package natia.skytweaks.features.dungeons

class TerminalHighlight/*private Minecraft mc = Minecraft.getMinecraft();

    Map<BlockPos, Boolean> highlights = new HashMap<>();
    private boolean startChecks = false;

    // [BOSS] Necron: Finally, I heard so much about you. The Eye likes you very much.
    public void chat(ClientChatReceivedEvent event) {
        if (!SkyTweaksConfig.terminalHighlight) return;

        if (event.message.getUnformattedText().startsWith("[BOSS]")) {
            // starting phase
            if (event.message.getUnformattedText().contains("CRAP!!")) {
                startChecks = true;
            } else if (event.message.getUnformattedText().contains("FACTORY'S CORE!")) {
                highlights.clear();
                startChecks = false;
            }
        }
    }
    int ticks = 0;

    public void blockRender(DrawBlockHighlightEvent event) {
        if (!SkyTweaksConfig.terminalHighlight) return;
        if (!startChecks) return;
        ticks++;

        BlockPos playerPos = new BlockPos(mc.thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ);
        if (ticks % 10 == 0) {
            highlights.clear();
            SecretUtils.getBlocksInBox(mc.theWorld,
                    new BlockPos(playerPos.getX() + 30, playerPos.getY() + 10, playerPos.getZ() + 30),
                    new BlockPos(playerPos.getX() - 30, playerPos.getY() - 10, playerPos.getZ() - 30))
                    .forEach((block, pos) -> {
                        if (block.equals(Blocks.command_block)) {
                            for (EntityArmorStand armorStand : mc.theWorld.getEntitiesWithinAABB(EntityArmorStand.class,
                                    new AxisAlignedBB(pos.getX() + 5, pos.getY() + 3, pos.getZ() + 5,
                                            pos.getX() - 5, pos.getY() - 3, pos.getZ() - 5))) {
                                if (StringUtils.stripControlCodes(armorStand.getName()).startsWith("Inactive Terminal")) {
                                    highlights.putIfAbsent(pos, true);
                                    break;
                                } else if (StringUtils.stripControlCodes(armorStand.getName()).startsWith("Terminal Active")) {
                                    highlights.putIfAbsent(pos, false);
                                    break;
                                }
                            }
                        }
                    });
        }

        // if true, is inactive. if false, is active
        highlights.forEach((pos, highlightType) -> {
            if (!highlightType)
                RenderUtils.highlightBlock(new Vector3f(pos.getX(), pos.getY(), pos.getZ()), 0.5f, event.partialTicks, new Color(SkyTweaksConfig.terminalHighlightColorACTIVE));
            else
                RenderUtils.highlightBlock(new Vector3f(pos.getX(), pos.getY(), pos.getZ()), 0.5f, event.partialTicks, new Color(SkyTweaksConfig.terminalHighlightColorINACTIVE));
        });
    }

    public void worldLoad() {
        highlights.clear();
        startChecks = false;
    }

    private static TerminalHighlight INSTANCE = new TerminalHighlight();
    public static TerminalHighlight getInstance() {
        return INSTANCE;
    }*/
