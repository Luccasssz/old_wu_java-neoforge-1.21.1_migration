package functionhook.oldwu.client.render;

import functionhook.oldwu.client.model.AngryCatBabyModel;
import functionhook.oldwu.client.model.AngryCatModel;
import functionhook.oldwu.client.model.BattleCatBabyModel;
import functionhook.oldwu.client.model.BattleCatModel;
import functionhook.oldwu.client.model.FlatCatBabyModel;
import functionhook.oldwu.client.model.FlatCatModel;
import functionhook.oldwu.client.model.MaodieCatModel;
import functionhook.oldwu.client.model.RecoveryCatBabyModel;
import functionhook.oldwu.client.model.RecoveryCatModel;
import functionhook.oldwu.client.model.GroomingCatBabyModel;
import functionhook.oldwu.client.model.GroomingCatModel;
import functionhook.oldwu.client.model.HitGroundCatBabyModel;
import functionhook.oldwu.client.model.HitGroundCatModel;

public interface CatStateModelHolder {
	MaodieCatModel oldwu_getMaodieModel();

	AngryCatModel oldwu_getAngryModel();

	AngryCatBabyModel oldwu_getAngryBabyModel();

	BattleCatModel oldwu_getBattleModel();

	BattleCatBabyModel oldwu_getBattleBabyModel();

	RecoveryCatModel oldwu_getRecoveryModel();

	RecoveryCatBabyModel oldwu_getRecoveryBabyModel();

	FlatCatModel oldwu_getFlatModel();

	FlatCatBabyModel oldwu_getFlatBabyModel();

	GroomingCatModel oldwu_getGroomingModel();

	GroomingCatBabyModel oldwu_getGroomingBabyModel();

	HitGroundCatModel oldwu_getHitGroundModel();

	HitGroundCatBabyModel oldwu_getHitGroundBabyModel();
}
