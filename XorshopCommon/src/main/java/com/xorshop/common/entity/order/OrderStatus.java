package com.xorshop.common.entity.order;

public enum OrderStatus {
	
	NEW {
		@Override
		public String defaultDescription() {
			return "La commande a été passée par le client";
		}

	}, 

	CANCELLED {
		@Override
		public String defaultDescription() {
			return "La commande a été rejetée";
		}
	}, 

	PROCESSING {
		@Override
		public String defaultDescription() {
			return "La commande est en cours de traitement";
		}
	},

	PACKAGED {
		@Override
		public String defaultDescription() {
			return "Les produits ont été emballés";
		}		
	}, 

	PICKED {
		@Override
		public String defaultDescription() {
			return "Le livreur a récupéré le colis";
		}		
	}, 

	SHIPPING {
		@Override
		public String defaultDescription() {
			return "L'expéditeur livre le colis";
		}		
	},

	DELIVERED {
		@Override
		public String defaultDescription() {
			return "Le client a reçu les produits";
		}		
	}, 

	RETURNED {
		@Override
		public String defaultDescription() {
			return "Les produits ont été retournés";
		}		
	}, 

	PAID {
		@Override
		public String defaultDescription() {
			return "Le client a payé cette commande";
		}		
	}, 

	REFUNDED {
		@Override
		public String defaultDescription() {
			return "Le client a été remboursé";
		}		
	},
	
	RETURN_REQUESTED {
		@Override
		public String defaultDescription() {
			return "Le client a envoyé une demande de retour d'achat";
		}		
	};

	public abstract String defaultDescription();
}
