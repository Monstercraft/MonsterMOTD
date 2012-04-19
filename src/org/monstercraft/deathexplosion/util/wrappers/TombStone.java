package org.monstercraft.deathexplosion.util.wrappers;

import org.bukkit.block.Block;

public class TombStone {
	private Block block;
	private Block sign;

	public TombStone(Block block, Block sign) {
		this.block = block;
		this.sign = sign;

	}

	public Block getBlock() {
		return block;
	}

	public Block getSign() {
		return sign;
	}
}