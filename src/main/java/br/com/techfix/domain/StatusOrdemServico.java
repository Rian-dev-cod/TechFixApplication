package br.com.techfix.domain;


import java.util.EnumSet;
import java.util.Set;

public enum StatusOrdemServico {
    RECEBIDO {
        @Override
        public Set<StatusOrdemServico> proximos(){
            return EnumSet.of(EM_ANALISE,CANCELADO);
        }
        },
    EM_ANALISE{
        @Override
        public Set<StatusOrdemServico> proximos(){
            return EnumSet.of(AGUARDANDO_APROVACAO,CANCELADO);
        }
    }  ,
    AGUARDANDO_APROVACAO {
        @Override
        public Set<StatusOrdemServico> proximos(){
            return EnumSet.of(EM_CONSERTO,CANCELADO);
        }
    } ,
    EM_CONSERTO {
        @Override
        public Set<StatusOrdemServico> proximos(){
            return EnumSet.of(PRONTO);
        }
    },
    PRONTO {
        @Override
        public Set<StatusOrdemServico> proximos(){
            return EnumSet.of(ENTREGUE);
        }
    },
    ENTREGUE {
        @Override
        public Set<StatusOrdemServico> proximos(){
            return EnumSet.noneOf(StatusOrdemServico.class);
        }
    } ,
    CANCELADO {
        @Override
        public Set<StatusOrdemServico> proximos(){
            return EnumSet.noneOf(StatusOrdemServico.class);
        }
    };


    public abstract Set<StatusOrdemServico> proximos();

    public boolean podeIrPara(StatusOrdemServico novoStatus){
        return proximos().contains(novoStatus);
    }



}
