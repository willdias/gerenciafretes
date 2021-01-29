package br.com.gerenciafretes.listeners;

import com.sankhya.util.TimeUtils;

import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.PersistenceEventAdapter;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;

public class GenrenciaFretesListener extends PersistenceEventAdapter {

	/** * */
	private static final long serialVersionUID = 1L;

	@Override
	public void beforeInsert(PersistenceEvent event) throws Exception {

		DynamicVO newRegistro = (DynamicVO) event.getVo();

		newRegistro.setProperty("DTALTER", TimeUtils.getNow());
		newRegistro.setProperty("ATIVO", "S");

		JapeWrapper parceiroDAO = JapeFactory.dao("Parceiro");
		if (newRegistro.asBigDecimal("CODPARC") != null) {
			String nomeparc = parceiroDAO.findByPK(newRegistro.asBigDecimal("CODPARC")).asString("NOMEPARC");
			newRegistro.setProperty("NOME", nomeparc);
		}

	}

	@Override
	public void beforeUpdate(PersistenceEvent event) throws Exception {

		DynamicVO newRegistro = (DynamicVO) event.getVo();
		DynamicVO oldRegistro = (DynamicVO) event.getOldVO();

		newRegistro.setProperty("DTALTER", TimeUtils.getNow());

		if (!newRegistro.asString("ATIVO").equals(oldRegistro.asString("ATIVO"))) {
			String log = "O campo ativo foi alterado de " + oldRegistro.asString("ATIVO") + " para "
					+ newRegistro.asString("ATIVO");
			newRegistro.setProperty("OBSERVACAO", log.toCharArray());
		}
	}

}
